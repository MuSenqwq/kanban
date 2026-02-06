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

    // 任务池查询
    public List<KbCard> selectTaskPoolList(Long userId);

    // 统计相关接口
    public Map<String, Object> selectUserTaskStats(Long userId);
    public List<KbCard> selectUpcomingTasks(Long userId);
    public int countUnreadMessages(Long userId);
    public List<KbCard> selectTomorrowDeadlineCards();

    // 全量可指派用户（兼容保留）
    public List<AssignUser> selectAllAssignUser();

    // ✅ 新增：按 cardId 返回该任务所在看板的成员（用于指派弹窗）
    public List<AssignUser> selectAssignableUsersByCardId(Long cardId);
}
