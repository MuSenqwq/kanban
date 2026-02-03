package com.ruoyi.kanban.mapper;

import java.util.List;
import com.ruoyi.kanban.domain.KbCard;

/**
 * 任务卡片Mapper接口
 * 
 * @author ruoyi
 * @date 2026-02-03
 */
public interface KbCardMapper 
{
    /**
     * 查询任务卡片
     * 
     * @param cardId 任务卡片主键
     * @return 任务卡片
     */
    public KbCard selectKbCardByCardId(Long cardId);

    /**
     * 查询任务卡片列表
     * 
     * @param kbCard 任务卡片
     * @return 任务卡片集合
     */
    public List<KbCard> selectKbCardList(KbCard kbCard);

    /**
     * 新增任务卡片
     * 
     * @param kbCard 任务卡片
     * @return 结果
     */
    public int insertKbCard(KbCard kbCard);

    /**
     * 修改任务卡片
     * 
     * @param kbCard 任务卡片
     * @return 结果
     */
    public int updateKbCard(KbCard kbCard);

    /**
     * 删除任务卡片
     * 
     * @param cardId 任务卡片主键
     * @return 结果
     */
    public int deleteKbCardByCardId(Long cardId);

    /**
     * 批量删除任务卡片
     * 
     * @param cardIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKbCardByCardIds(String[] cardIds);
}
