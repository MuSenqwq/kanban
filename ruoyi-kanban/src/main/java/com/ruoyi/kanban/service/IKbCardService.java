package com.ruoyi.kanban.service;

import java.util.List;
import com.ruoyi.kanban.domain.KbCard;

public interface IKbCardService
{
    public KbCard selectKbCardByCardId(Long cardId);
    public List<KbCard> selectKbCardList(KbCard kbCard);
    public int insertKbCard(KbCard kbCard);
    public int updateKbCard(KbCard kbCard);
    public int deleteKbCardByCardIds(String cardIds);
    public int deleteKbCardByCardId(Long cardId);

    // 拖拽排序
    public int changeCardOrder(Long cardId, Long listId, String sortOrder);

    // 查询待认领任务列表
    public List<KbCard> selectTaskPoolList(Long userId);

    // 认领任务
    public int claimTask(Long cardId, Long userId);

    // 完成任务
    public int completeTask(Long cardId);
}