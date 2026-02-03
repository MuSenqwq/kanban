package com.ruoyi.kanban.service;

import java.util.List;
import com.ruoyi.kanban.domain.KbList;

/**
 * 看板任务列Service接口
 * 
 * @author ruoyi
 * @date 2026-02-03
 */
public interface IKbListService 
{
    /**
     * 查询看板任务列
     * 
     * @param listId 看板任务列主键
     * @return 看板任务列
     */
    public KbList selectKbListByListId(Long listId);

    /**
     * 查询看板任务列列表
     * 
     * @param kbList 看板任务列
     * @return 看板任务列集合
     */
    public List<KbList> selectKbListList(KbList kbList);

    /**
     * 新增看板任务列
     * 
     * @param kbList 看板任务列
     * @return 结果
     */
    public int insertKbList(KbList kbList);

    /**
     * 修改看板任务列
     * 
     * @param kbList 看板任务列
     * @return 结果
     */
    public int updateKbList(KbList kbList);

    /**
     * 批量删除看板任务列
     * 
     * @param listIds 需要删除的看板任务列主键集合
     * @return 结果
     */
    public int deleteKbListByListIds(String listIds);

    /**
     * 删除看板任务列信息
     * 
     * @param listId 看板任务列主键
     * @return 结果
     */
    public int deleteKbListByListId(Long listId);
}
