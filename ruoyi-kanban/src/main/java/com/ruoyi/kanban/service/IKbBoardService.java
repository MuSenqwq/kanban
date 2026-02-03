package com.ruoyi.kanban.service;

import java.util.List;
import com.ruoyi.kanban.domain.KbBoard;

/**
 * 任务看板Service接口
 * 
 * @author ruoyi
 * @date 2026-02-03
 */
public interface IKbBoardService 
{
    /**
     * 查询任务看板
     * 
     * @param boardId 任务看板主键
     * @return 任务看板
     */
    public KbBoard selectKbBoardByBoardId(Long boardId);

    /**
     * 查询任务看板列表
     * 
     * @param kbBoard 任务看板
     * @return 任务看板集合
     */
    public List<KbBoard> selectKbBoardList(KbBoard kbBoard);

    /**
     * 新增任务看板
     * 
     * @param kbBoard 任务看板
     * @return 结果
     */
    public int insertKbBoard(KbBoard kbBoard);

    /**
     * 修改任务看板
     * 
     * @param kbBoard 任务看板
     * @return 结果
     */
    public int updateKbBoard(KbBoard kbBoard);

    /**
     * 批量删除任务看板
     * 
     * @param boardIds 需要删除的任务看板主键集合
     * @return 结果
     */
    public int deleteKbBoardByBoardIds(String boardIds);

    /**
     * 删除任务看板信息
     * 
     * @param boardId 任务看板主键
     * @return 结果
     */
    public int deleteKbBoardByBoardId(Long boardId);
}
