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

    /**
     * 拖拽排序（兼容旧版：只传目标列顺序）
     */
    default int changeCardOrder(Long cardId, Long listId, String sortOrder)
    {
        return changeCardOrder(cardId, listId, sortOrder, null, null);
    }

    /**
     * 拖拽排序（新版：同时传目标列/来源列顺序，避免排序错乱）
     *
     * @param cardId        被移动卡片ID
     * @param listId        目标列ID
     * @param sortOrder     目标列卡片ID顺序（逗号分隔）
     * @param fromListId    来源列ID（可空）
     * @param fromSortOrder 来源列卡片ID顺序（逗号分隔，可空）
     */
    public int changeCardOrder(Long cardId, Long listId, String sortOrder, Long fromListId, String fromSortOrder);

    // 查询待认领任务列表
    public List<KbCard> selectTaskPoolList(Long userId);

    // 认领任务
    public int claimTask(Long cardId, Long userId);

    // 完成任务
    public int completeTask(Long cardId);

    // 查询指派名单
    public List<AssignUser> selectAllAssignUser();
}
