package com.ruoyi.kanban.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.kanban.domain.AssignUser;
import com.ruoyi.kanban.domain.KbBoard;
import com.ruoyi.kanban.domain.KbBoardMember;
import com.ruoyi.kanban.domain.KbCard;
import com.ruoyi.kanban.domain.KbList;
import com.ruoyi.kanban.mapper.KbBoardMapper;
import com.ruoyi.kanban.mapper.KbBoardMemberMapper;
import com.ruoyi.kanban.mapper.KbCardMapper;
import com.ruoyi.kanban.service.IKbCardService;
import com.ruoyi.kanban.service.IKbListService;
import com.ruoyi.system.service.ISysUserMessageService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务卡片Service业务层处理
 */
@Service
public class KbCardServiceImpl implements IKbCardService
{
    @Autowired
    private KbCardMapper kbCardMapper;

    @Autowired
    private KbBoardMapper kbBoardMapper;

    @Autowired
    private KbBoardMemberMapper kbBoardMemberMapper;

    @Autowired
    private IKbListService kbListService;

    @Autowired
    private ISysUserMessageService messageService;

    @Autowired
    private ISysUserService userService;

    @Override
    public KbCard selectKbCardByCardId(Long cardId) {
        return kbCardMapper.selectKbCardByCardId(cardId);
    }

    @Override
    public List<KbCard> selectKbCardList(KbCard kbCard) {
        return kbCardMapper.selectKbCardList(kbCard);
    }

    /**
     * 新增任务卡片（包含指派通知）
     */
    @Override
    public int insertKbCard(KbCard kbCard) {
        kbCard.setCreateTime(DateUtils.getNowDate());
        int rows = kbCardMapper.insertKbCard(kbCard);

        // 创建时指定执行人：通知执行人
        if (rows > 0 && kbCard.getExecutorId() != null) {
            messageService.sendTaskMessage(
                    kbCard.getExecutorId(),
                    "新任务指派",
                    "您被指派了新任务：" + kbCard.getCardTitle()
            );
        }
        return rows;
    }

    @Override
    public int updateKbCard(KbCard kbCard) {
        kbCard.setUpdateTime(DateUtils.getNowDate());
        return kbCardMapper.updateKbCard(kbCard);
    }

    @Override
    public int deleteKbCardByCardIds(String cardIds) {
        return kbCardMapper.deleteKbCardByCardIds(Convert.toStrArray(cardIds));
    }

    @Override
    public int deleteKbCardByCardId(Long cardId) {
        return kbCardMapper.deleteKbCardByCardId(cardId);
    }

    /**
     * 拖拽排序 / 跨列移动（保存目标列顺序 + 来源列顺序）
     */
    @Override
    @Transactional
    public int changeCardOrder(Long cardId, Long listId, String sortOrder, Long fromListId, String fromSortOrder) {

        if (cardId == null || listId == null) {
            throw new ServiceException("参数错误：cardId/listId不能为空");
        }

        KbCard dbCard = kbCardMapper.selectKbCardByCardId(cardId);
        if (dbCard == null) {
            throw new ServiceException("任务不存在");
        }

        // 归档不允许拖拽
        if ("2".equals(dbCard.getStatus())) {
            throw new ServiceException("已归档任务无法拖拽");
        }

        // 权限校验：系统管理员 / 看板负责人 / 看板成员
        Long userId = ShiroUtils.getUserId();
        checkBoardPermission(dbCard.getBoardId(), userId);

        Long oldListId = dbCard.getListId();
        boolean listChanged = oldListId != null && !oldListId.equals(listId);

        // 1) 更新被拖拽卡片归属列
        KbCard moved = new KbCard();
        moved.setCardId(cardId);
        moved.setListId(listId);
        moved.setUpdateBy(ShiroUtils.getLoginName());
        moved.setUpdateTime(DateUtils.getNowDate());

        // [可选] 拖到“完成”列自动完成
        KbList targetList = kbListService.selectKbListByListId(listId);
        if (targetList != null && isDoneListName(targetList.getListName())) {
            moved.setStatus("1");
            moved.setProgress(100);
            moved.setFinishTime(DateUtils.getNowDate());
        }

        kbCardMapper.updateKbCard(moved);

        // 2) 目标列按 sortOrder 重排
        applyListOrder(listId, sortOrder);

        // 3) 跨列：来源列也要重排
        if (listChanged) {
            if (fromListId != null && fromListId.equals(oldListId) && StringUtils.isNotEmpty(fromSortOrder)) {
                applyListOrder(oldListId, fromSortOrder);
            } else {
                normalizeListOrder(oldListId);
            }
        }

        return 1;
    }

    /**
     * ✅ 修复点：你项目的 SysUser.isAdmin() 是无参方法，所以这里改为实例判断
     */
    private void checkBoardPermission(Long boardId, Long userId) {
        if (boardId == null) {
            throw new ServiceException("任务数据异常：boardId为空");
        }
        if (userId == null) {
            throw new ServiceException("未登录");
        }

        // 系统管理员（兼容不同RuoYi版本）
        if (isSuperAdmin(userId)) {
            return;
        }

        // 看板负责人（创建者）
        KbBoard board = kbBoardMapper.selectKbBoardByBoardId(boardId);
        if (board != null && board.getUserId() != null && board.getUserId().equals(userId)) {
            return;
        }

        // 看板成员
        KbBoardMember query = new KbBoardMember();
        query.setBoardId(boardId);
        query.setUserId(userId);
        List<KbBoardMember> members = kbBoardMemberMapper.selectKbBoardMemberList(query);
        if (members != null && !members.isEmpty()) {
            return;
        }

        throw new ServiceException("无权限操作该看板任务");
    }

    /**
     * 兼容：不同RuoYi版本 admin 判断方式不一样
     */
    private boolean isSuperAdmin(Long userId) {
        try {
            SysUser u = ShiroUtils.getSysUser();
            if (u != null) {
                // 你这个版本：isAdmin() 无参
                return u.isAdmin();
            }
        } catch (Exception ignored) {
        }
        // 兜底：RuoYi 常见超管 userId=1
        return userId != null && userId.longValue() == 1L;
    }

    /**
     * 按前端传入顺序重排（同时修正 list_id）
     */
    private void applyListOrder(Long listId, String orderStr) {
        if (listId == null) return;

        if (StringUtils.isNotEmpty(orderStr)) {
            String[] cardIds = Convert.toStrArray(orderStr);
            long idx = 0L;
            for (String cid : cardIds) {
                if (StringUtils.isEmpty(cid)) continue;
                KbCard c = new KbCard();
                c.setCardId(Long.valueOf(cid));
                c.setListId(listId);
                c.setOrderNum(idx++);
                c.setUpdateTime(DateUtils.getNowDate());
                kbCardMapper.updateKbCard(c);
            }
        } else {
            normalizeListOrder(listId);
        }
    }

    /**
     * 从DB读取当前列卡片并重排 order_num
     */
    private void normalizeListOrder(Long listId) {
        if (listId == null) return;
        List<Long> ids = kbCardMapper.selectCardIdsByListId(listId);
        if (ids == null) return;

        long idx = 0L;
        for (Long id : ids) {
            if (id == null) continue;
            KbCard c = new KbCard();
            c.setCardId(id);
            c.setOrderNum(idx++);
            c.setUpdateTime(DateUtils.getNowDate());
            kbCardMapper.updateKbCard(c);
        }
    }

    private boolean isDoneListName(String listName) {
        if (StringUtils.isEmpty(listName)) return false;
        String n = listName.trim();
        return n.contains("完成") || n.equalsIgnoreCase("done") || n.equalsIgnoreCase("completed");
    }

    @Override
    public List<KbCard> selectTaskPoolList(Long userId,String cardTitle) {
        return kbCardMapper.selectTaskPoolList(userId,cardTitle );
    }

    /**
     * 认领任务（防并发锁 + 认领通知）
     */
    @Override
    @Transactional
    public synchronized int claimTask(Long cardId, Long userId) {
        KbCard card = kbCardMapper.selectKbCardByCardId(cardId);
        if (card == null) {
            throw new ServiceException("任务不存在");
        }
        if (card.getExecutorId() != null && card.getExecutorId() > 0) {
            throw new ServiceException("手慢了！该任务已被认领/指派");
        }

        card.setExecutorId(userId);
        card.setStatus("0");
        card.setUpdateTime(DateUtils.getNowDate());
        int rows = kbCardMapper.updateKbCard(card);

        // 通知创建者：任务被认领
        if (rows > 0 && StringUtils.isNotEmpty(card.getCreateBy())) {
            SysUser creator = userService.selectUserByLoginName(card.getCreateBy());
            if (creator != null && !creator.getUserId().equals(userId)) {
                messageService.sendTaskMessage(
                        creator.getUserId(),
                        "任务认领通知",
                        "您的任务 [" + card.getCardTitle() + "] 已被认领"
                );
            }
        }
        return rows;
    }

    /**
     * 完成任务（包含完成通知）
     */
    @Override
    public int completeTask(Long cardId) {
        KbCard existCard = kbCardMapper.selectKbCardByCardId(cardId);

        KbCard card = new KbCard();
        card.setCardId(cardId);
        card.setStatus("1");
        card.setProgress(100);
        card.setFinishTime(DateUtils.getNowDate());
        card.setUpdateTime(DateUtils.getNowDate());
        int rows = kbCardMapper.updateKbCard(card);

        if (rows > 0 && existCard != null && StringUtils.isNotEmpty(existCard.getCreateBy())) {
            SysUser creator = userService.selectUserByLoginName(existCard.getCreateBy());
            if (creator != null && (existCard.getExecutorId() == null || !creator.getUserId().equals(existCard.getExecutorId()))) {
                messageService.sendTaskMessage(
                        creator.getUserId(),
                        "任务完成通知",
                        "任务 [" + existCard.getCardTitle() + "] 已完成"
                );
            }
        }
        return rows;
    }

    @Override
    public List<AssignUser> selectAllAssignUser() {
        return kbCardMapper.selectAllAssignUser();
    }
    @Override
    public int updateKbCardProgress(KbCard kbCard) {
        // 直接更新进度字段（需确保数据库表中有progress字段）
        return kbCardMapper.updateKbCardProgress(kbCard);
    }
}
