package com.ruoyi.kanban.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.kanban.mapper.KbCardMapper;
import com.ruoyi.kanban.domain.KbCard;
import com.ruoyi.kanban.service.IKbCardService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务卡片Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-03
 */
@Service
public class KbCardServiceImpl implements IKbCardService 
{
    @Autowired
    private KbCardMapper kbCardMapper;

    /**
     * 查询任务卡片
     * 
     * @param cardId 任务卡片主键
     * @return 任务卡片
     */
    @Override
    public KbCard selectKbCardByCardId(Long cardId)
    {
        return kbCardMapper.selectKbCardByCardId(cardId);
    }

    /**
     * 查询任务卡片列表
     * 
     * @param kbCard 任务卡片
     * @return 任务卡片
     */
    @Override
    public List<KbCard> selectKbCardList(KbCard kbCard)
    {
        return kbCardMapper.selectKbCardList(kbCard);
    }

    /**
     * 新增任务卡片
     * 
     * @param kbCard 任务卡片
     * @return 结果
     */
    @Override
    public int insertKbCard(KbCard kbCard)
    {
        kbCard.setCreateTime(DateUtils.getNowDate());
        return kbCardMapper.insertKbCard(kbCard);
    }

    /**
     * 修改任务卡片
     * 
     * @param kbCard 任务卡片
     * @return 结果
     */
    @Override
    public int updateKbCard(KbCard kbCard)
    {
        kbCard.setUpdateTime(DateUtils.getNowDate());
        return kbCardMapper.updateKbCard(kbCard);
    }

    /**
     * 批量删除任务卡片
     * 
     * @param cardIds 需要删除的任务卡片主键
     * @return 结果
     */
    @Override
    public int deleteKbCardByCardIds(String cardIds)
    {
        return kbCardMapper.deleteKbCardByCardIds(Convert.toStrArray(cardIds));
    }

    /**
     * 删除任务卡片信息
     * 
     * @param cardId 任务卡片主键
     * @return 结果
     */
    @Override
    public int deleteKbCardByCardId(Long cardId)
    {
        return kbCardMapper.deleteKbCardByCardId(cardId);
    }


    /**
     * (新增) 拖拽排序核心逻辑
     */
    @Override
    @Transactional
    public int changeCardOrder(Long cardId, Long listId, String sortOrder)
    {
        // 1. 更新当前被拖拽卡片的归属列
        KbCard currentCard = new KbCard();
        currentCard.setCardId(cardId);
        currentCard.setListId(listId);
        currentCard.setUpdateTime(DateUtils.getNowDate());
        kbCardMapper.updateKbCard(currentCard);

        // 2. 如果传来了新的排序字串，更新该列所有卡片的 orderNum
        if (StringUtils.isNotEmpty(sortOrder)) {
            String[] cardIds = Convert.toStrArray(sortOrder);
            for (int i = 0; i < cardIds.length; i++) {
                if (StringUtils.isNotEmpty(cardIds[i])) {
                    KbCard c = new KbCard();
                    c.setCardId(Long.valueOf(cardIds[i]));
                    c.setOrderNum((long) i); // 下标即为新的排序号
                    kbCardMapper.updateKbCard(c);
                }
            }
        }
        return 1;
    }
}
