package com.ruoyi.kanban.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.kanban.domain.AssignUser;
import com.ruoyi.kanban.domain.KbBoard;
import com.ruoyi.kanban.domain.KbBoardMember;
import com.ruoyi.kanban.domain.KbCard;
import com.ruoyi.kanban.mapper.KbBoardMapper;
import com.ruoyi.kanban.mapper.KbBoardMemberMapper;
import com.ruoyi.kanban.mapper.KbCardMapper;
import com.ruoyi.kanban.service.IKbCardService;
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
    private ISysUserMessageService messageService; // 消息服务

    @Autowired
    private ISysUserService userService; // 用户服务

    @Override
    public KbCard selectKbCardByCardId(Long cardId) {
        return kbCardMapper.selectKbCardByCardId(cardId);
    }

    @Override
    public List<KbCard> selectKbCardList(KbCard kbCard) {
        return kbCardMapper.selectKbCardList(kbCard);
    }

    /**
     * 新增任务卡片（包含指派通知：若创建时就有 executorId）
     */
    @Override
    public int insertKbCard(KbCard kbCard) {
        kbCard.setCreateTime(DateUtils.getNowDate());
        int rows = kbCardMapper.insertKbCard(kbCard);

        // 如果创建时指定了执行人，给执行人发消息
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

    @Override
    @Transactional
    public int changeCardOrder(Long cardId, Long listId, String sortOrder) {
        KbCard currentCard = new KbCard();
        currentCard.setCardId(cardId);
        currentCard.setListId(listId);
        currentCard.setUpdateTime(DateUtils.getNowDate());
        kbCardMapper.updateKbCard(currentCard);

        if (StringUtils.isNotEmpty(sortOrder)) {
            String[] cardIds = Convert.toStrArray(sortOrder);
            for (int i = 0; i < cardIds.length; i++) {
                if (StringUtils.isNotEmpty(cardIds[i])) {
                    KbCard c = new KbCard();
                    c.setCardId(Long.valueOf(cardIds[i]));
                    c.setOrderNum((long) i);
                    kbCardMapper.updateKbCard(c);
                }
            }
        }
        return 1;
    }

    @Override
    public List<KbCard> selectTaskPoolList(Long userId) {
        return kbCardMapper.selectTaskPoolList(userId);
    }

    /**
     * 认领任务（防并发锁 + 权限校验 + 认领通知）
     */
    @Override
    @Transactional
    public synchronized int claimTask(Long cardId, Long userId) {
        KbCard card = kbCardMapper.selectKbCardByCardId(cardId);
        if (card == null) {
            throw new ServiceException("任务不存在");
        }

        // 权限校验：必须是该任务所在看板成员/看板创建者/任务创建者/系统管理员
        checkCardOperatePermission(card, userId);

        if (card.getExecutorId() != null && card.getExecutorId() > 0) {
            throw new ServiceException("手慢了！该任务已被认领/指派");
        }

        card.setExecutorId(userId);
        card.setStatus("0"); // 自动变更为进行中
        card.setUpdateTime(DateUtils.getNowDate());
        int rows = kbCardMapper.updateKbCard(card);

        // 认领成功后，通知任务创建者
        if (rows > 0 && StringUtils.isNotEmpty(card.getCreateBy())) {
            SysUser creator = userService.selectUserByLoginName(card.getCreateBy());
            // 如果创建者存在，且不是自己认领自己的任务
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
     * 指派任务（防并发锁 + 权限校验 + 指派通知发给执行人）
     */
    @Override
    @Transactional
    public synchronized int assignTask(Long cardId, Long executorId, Long operatorId) {
        if (executorId == null || executorId <= 0) {
            throw new ServiceException("请选择有效的执行人");
        }

        KbCard card = kbCardMapper.selectKbCardByCardId(cardId);
        if (card == null) {
            throw new ServiceException("任务不存在");
        }

        // 权限校验：只有看板管理员/看板创建者/系统管理员可指派
        checkBoardAdminPermission(card.getBoardId(), operatorId);

        // 只能指派“未分配”的任务（任务池逻辑）
        if (card.getExecutorId() != null && card.getExecutorId() > 0) {
            throw new ServiceException("该任务已被认领/指派，无法重复指派");
        }

        // 校验执行人必须是该看板成员（或系统管理员）
        if (!isBoardMemberOrOwner(card.getBoardId(), executorId) && !ShiroUtils.isAdmin(executorId)) {
            throw new ServiceException("该用户不是看板成员，无法指派");
        }

        card.setExecutorId(executorId);
        card.setStatus("0");
        card.setUpdateTime(DateUtils.getNowDate());
        int rows = kbCardMapper.updateKbCard(card);

        // 指派成功后，通知执行人（不是自己指派给自己才发）
        if (rows > 0 && !executorId.equals(operatorId)) {
            messageService.sendTaskMessage(
                    executorId,
                    "任务指派通知",
                    "您被指派了任务：[" + card.getCardTitle() + "]"
            );
        }
        return rows;
    }

    /**
     * 一键完成任务（包含完成通知）
     */
    @Override
    public int completeTask(Long cardId) {
        // 先查出来获取信息用于通知
        KbCard existCard = kbCardMapper.selectKbCardByCardId(cardId);

        KbCard card = new KbCard();
        card.setCardId(cardId);
        card.setStatus("1"); // 1=已完成
        card.setProgress(100);
        card.setFinishTime(DateUtils.getNowDate());
        card.setUpdateTime(DateUtils.getNowDate());
        int rows = kbCardMapper.updateKbCard(card);

        // 完成通知 (通知创建者)
        if (rows > 0 && existCard != null && StringUtils.isNotEmpty(existCard.getCreateBy())) {
            SysUser creator = userService.selectUserByLoginName(existCard.getCreateBy());
            // 如果执行人不是创建者，通知创建者
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

    /**
     * 查询当前任务所在看板的可指派成员（带权限校验：至少得能操作该任务）
     */
    @Override
    public List<AssignUser> selectAssignableUsers(Long cardId, Long operatorId) {
        KbCard card = kbCardMapper.selectKbCardByCardId(cardId);
        if (card == null) {
            throw new ServiceException("任务不存在");
        }
        // 至少是看板成员/创建者/系统管理员才能看到可指派成员
        checkCardOperatePermission(card, operatorId);
        return kbCardMapper.selectAssignableUsersByCardId(cardId);
    }

    /**
     * 兼容保留：全量用户
     */
    @Override
    public List<AssignUser> selectAllAssignUser() {
        return kbCardMapper.selectAllAssignUser();
    }

    // =========================
    // 权限校验工具方法
    // =========================

    /**
     * 任务操作权限：系统管理员 / 看板创建者 / 看板成员 / 任务创建者
     */
    private void checkCardOperatePermission(KbCard card, Long userId) {
        if (userId == null) {
            throw new ServiceException("未登录");
        }
        if (ShiroUtils.isAdmin(userId)) {
            return;
        }
        if (card.getBoardId() == null) {
            throw new ServiceException("任务数据异常：缺少boardId");
        }

        // 看板创建者
        KbBoard board = kbBoardMapper.selectKbBoardByBoardId(card.getBoardId());
        if (board != null && userId.equals(board.getUserId())) {
            return;
        }

        // 看板成员
        if (isBoardMember(card.getBoardId(), userId)) {
            return;
        }

        // 任务创建者（createBy=loginName）
        if (StringUtils.isNotEmpty(card.getCreateBy())) {
            SysUser creator = userService.selectUserByLoginName(card.getCreateBy());
            if (creator != null && userId.equals(creator.getUserId())) {
                return;
            }
        }

        throw new ServiceException("无权限操作该任务");
    }

    /**
     * 指派权限：系统管理员 / 看板创建者 / 看板成员(role=0 看板管理员)
     */
    private void checkBoardAdminPermission(Long boardId, Long operatorId) {
        if (operatorId == null) {
            throw new ServiceException("未登录");
        }
        if (ShiroUtils.isAdmin(operatorId)) {
            return;
        }
        if (boardId == null) {
            throw new ServiceException("看板不存在/数据异常");
        }

        KbBoard board = kbBoardMapper.selectKbBoardByBoardId(boardId);
        if (board != null && operatorId.equals(board.getUserId())) {
            return;
        }

        // role=0 表示看板管理员
        KbBoardMember q = new KbBoardMember();
        q.setBoardId(boardId);
        q.setUserId(operatorId);
        q.setRole("0");
        List<KbBoardMember> admins = kbBoardMemberMapper.selectKbBoardMemberList(q);
        if (admins != null && !admins.isEmpty()) {
            return;
        }

        throw new ServiceException("无权限指派：仅看板管理员可操作");
    }

    private boolean isBoardMember(Long boardId, Long userId) {
        KbBoardMember q = new KbBoardMember();
        q.setBoardId(boardId);
        q.setUserId(userId);
        List<KbBoardMember> list = kbBoardMemberMapper.selectKbBoardMemberList(q);
        return list != null && !list.isEmpty();
    }

    private boolean isBoardMemberOrOwner(Long boardId, Long userId) {
        if (boardId == null || userId == null) return false;
        KbBoard board = kbBoardMapper.selectKbBoardByBoardId(boardId);
        if (board != null && userId.equals(board.getUserId())) return true;
        return isBoardMember(boardId, userId);
    }
}
