package com.ruoyi.kanban.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.kanban.mapper.KbCardMapper;
import com.ruoyi.kanban.domain.KbCard;
import com.ruoyi.kanban.service.IKbCardService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KbCardServiceImpl implements IKbCardService
{
    @Autowired
    private KbCardMapper kbCardMapper;

    @Override
    public KbCard selectKbCardByCardId(Long cardId) {
        return kbCardMapper.selectKbCardByCardId(cardId);
    }

    @Override
    public List<KbCard> selectKbCardList(KbCard kbCard) {
        return kbCardMapper.selectKbCardList(kbCard);
    }

    @Override
    public int insertKbCard(KbCard kbCard) {
        kbCard.setCreateTime(DateUtils.getNowDate());
        return kbCardMapper.insertKbCard(kbCard);
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

    /**
     * 查询任务池
     */
    @Override
    public List<KbCard> selectTaskPoolList(Long userId) {
        return kbCardMapper.selectTaskPoolList(userId);
    }

    /**
     * 认领任务（防并发锁）
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
        return kbCardMapper.updateKbCard(card);
    }

    /**
     * 一键完成任务
     */
    @Override
    public int completeTask(Long cardId) {
        KbCard card = new KbCard();
        card.setCardId(cardId);
        card.setStatus("1"); // 1=已完成
        card.setProgress(100);
        card.setFinishTime(DateUtils.getNowDate());
        card.setUpdateTime(DateUtils.getNowDate());
        return kbCardMapper.updateKbCard(card);
    }
}