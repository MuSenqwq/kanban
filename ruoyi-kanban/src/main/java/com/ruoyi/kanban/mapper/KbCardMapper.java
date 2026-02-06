package com.ruoyi.kanban.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.kanban.domain.AssignUser;
import com.ruoyi.kanban.domain.KbCard;

public interface KbCardMapper
{
    public KbCard selectKbCardByCardId(Long cardId);
    public List<KbCard> selectKbCardList(KbCard kbCard);
    public int insertKbCard(KbCard kbCard);
    public int updateKbCard(KbCard kbCard);
    public int deleteKbCardByCardId(Long cardId);
    public int deleteKbCardByCardIds(String[] cardIds);

    // 新增：任务池查询
    public List<KbCard> selectTaskPoolList(Long userId);

    // 之前统计相关的接口
    public Map<String, Object> selectUserTaskStats(Long userId);
    public List<KbCard> selectUpcomingTasks(Long userId);
    public int countUnreadMessages(Long userId);
    public List<KbCard> selectTomorrowDeadlineCards();
    public List<AssignUser> selectAllAssignUser();
}