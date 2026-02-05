package com.ruoyi.kanban.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.kanban.domain.KbCard;

/**
 * 任务卡片Mapper接口
 * @author ruoyi
 */
public interface KbCardMapper
{
    /** 查询任务卡片 */
    public KbCard selectKbCardByCardId(Long cardId);

    /** 查询任务卡片列表 */
    public List<KbCard> selectKbCardList(KbCard kbCard);

    /** 新增任务卡片 */
    public int insertKbCard(KbCard kbCard);

    /** 修改任务卡片 */
    public int updateKbCard(KbCard kbCard);

    /** 删除任务卡片 */
    public int deleteKbCardByCardId(Long cardId);

    /** 批量删除任务卡片 */
    public int deleteKbCardByCardIds(String[] cardIds);

    /** [新增] 统计用户任务指标 */
    public Map<String, Object> selectUserTaskStats(Long userId);

    /** [新增] 查询即将到期任务 */
    public List<KbCard> selectUpcomingTasks(Long userId);

    /** [新增] 统计未读消息 */
    public int countUnreadMessages(Long userId);

    /** [新增] 查询明天截止的任务 */
    public List<KbCard> selectTomorrowDeadlineCards();
}