package com.ruoyi.kanban.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.core.domain.entity.SysUser; // 必须导入
import com.ruoyi.kanban.domain.AssignUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.kanban.mapper.KbCardMapper;
import com.ruoyi.kanban.domain.KbCard;
import com.ruoyi.kanban.service.IKbCardService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.service.ISysUserMessageService; // [新增] 必须导入
import com.ruoyi.system.service.ISysUserService;       // [新增] 必须导入

/**
 * 任务卡片Service业务层处理
 */
@Service
public class KbCardServiceImpl implements IKbCardService
{
    @Autowired
    private KbCardMapper kbCardMapper;

    @Autowired
    private ISysUserMessageService messageService; // [新增] 注入消息服务

    @Autowired
    private ISysUserService userService; // [新增] 注入用户服务

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

        // [新增逻辑] 如果创建时指定了执行人，给执行人发消息
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
            throw new ServiceException("手慢了！该任务已被认领");
        }
        card.setExecutorId(userId);
        card.setStatus("0"); // 自动变更为进行中
        card.setUpdateTime(DateUtils.getNowDate());
        int rows = kbCardMapper.updateKbCard(card);

        // [新增逻辑] 认领成功后，通知任务创建者
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

        // [新增逻辑] 完成通知 (通知创建者)
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

    @Override
    public List<AssignUser> selectAllAssignUser() {
        return kbCardMapper.selectAllAssignUser();
    }
}