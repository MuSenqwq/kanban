package com.ruoyi.kanban.service;

import java.util.List;
import com.ruoyi.kanban.domain.KbCard;

/**
 * 任务卡片Service接口
 * 
 * @author ruoyi
 * @date 2026-02-03
 */
public interface IKbCardService 
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
     * 批量删除任务卡片
     * 
     * @param cardIds 需要删除的任务卡片主键集合
     * @return 结果
     */
    public int deleteKbCardByCardIds(String cardIds);

    /**
     * 删除任务卡片信息
     * 
     * @param cardId 任务卡片主键
     * @return 结果
     */
    public int deleteKbCardByCardId(Long cardId);

    /**
     * (新增) 修改任务卡片排序和归属列
     * * @param cardId 当前被拖拽的卡片ID
     * @param listId 目标列ID
     * @param sortOrder 目标列中所有卡片的ID顺序字符串(逗号分隔)
     * @return 结果
     */
    public int changeCardOrder(Long cardId, Long listId, String sortOrder);
}



