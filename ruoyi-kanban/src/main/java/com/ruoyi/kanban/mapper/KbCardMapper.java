package com.ruoyi.kanban.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.kanban.domain.AssignUser;
import com.ruoyi.kanban.domain.KbCard;
import org.apache.ibatis.annotations.Param;

public interface KbCardMapper
{
    public KbCard selectKbCardByCardId(Long cardId);
    public List<KbCard> selectKbCardList(KbCard kbCard);
    public int insertKbCard(KbCard kbCard);
    public int updateKbCard(KbCard kbCard);
    public int deleteKbCardByCardId(Long cardId);
    public int deleteKbCardByCardIds(String[] cardIds);

    // 新增：任务池查询

    List<KbCard> selectTaskPoolList( @Param("userId") Long userId, @Param("cardTitle") String cardTitle);

    // 之前统计相关的接口
    public Map<String, Object> selectUserTaskStats(Long userId);
    public List<KbCard> selectUpcomingTasks(Long userId);
    public int countUnreadMessages(Long userId);
    public List<KbCard> selectTomorrowDeadlineCards();
    public List<AssignUser> selectAllAssignUser();

    /**
     * [新增] 查询某列下卡片ID（用于服务端兜底重排 order_num）
     */
    public List<Long> selectCardIdsByListId(Long listId);
    /**
     * 更新任务进度
     * @param kbCard 任务卡片
     * @return 影响行数
     */
    int updateKbCardProgress(KbCard kbCard);
}
