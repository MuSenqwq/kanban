package com.ruoyi.kanban.service;

import java.util.List;

import com.ruoyi.kanban.domain.AssignUser;
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

    // 指派任务（operatorId=操作人）
    public int assignTask(Long cardId, Long executorId, Long operatorId);

    // 完成任务
    public int completeTask(Long cardId);

    // 查询当前任务所在看板可指派成员（operatorId=操作人，用于权限校验）
    public List<AssignUser> selectAssignableUsers(Long cardId, Long operatorId);

    // 兼容保留：查询全量用户（如你别处还在用）
    public List<AssignUser> selectAllAssignUser();
}
