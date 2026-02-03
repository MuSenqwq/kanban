package com.ruoyi.kanban.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils; // [新增] 导入 ShiroUtils
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.kanban.mapper.KbBoardMapper;
import com.ruoyi.kanban.domain.KbBoard;
import com.ruoyi.kanban.service.IKbBoardService;
import com.ruoyi.common.core.text.Convert;

/**
 * 任务看板Service业务层处理
 * * @author ruoyi
 * @date 2026-02-03
 */
@Service
public class KbBoardServiceImpl implements IKbBoardService
{
    @Autowired
    private KbBoardMapper kbBoardMapper;

    /**
     * 查询任务看板
     * * @param boardId 任务看板主键
     * @return 任务看板
     */
    @Override
    public KbBoard selectKbBoardByBoardId(Long boardId)
    {
        return kbBoardMapper.selectKbBoardByBoardId(boardId);
    }

    /**
     * 查询任务看板列表
     * * @param kbBoard 任务看板
     * @return 任务看板
     */
    @Override
    public List<KbBoard> selectKbBoardList(KbBoard kbBoard)
    {
        // [新增] 只能查询自己的看板 (可选，为了安全建议加上)
        // kbBoard.setUserId(ShiroUtils.getUserId());
        return kbBoardMapper.selectKbBoardList(kbBoard);
    }

    /**
     * 新增任务看板
     * * @param kbBoard 任务看板
     * @return 结果
     */
    @Override
    public int insertKbBoard(KbBoard kbBoard)
    {
        // [修改核心点] 获取当前登录用户的ID并设置
        kbBoard.setUserId(ShiroUtils.getUserId());

        // [建议] 同时设置创建者账号，方便审计
        kbBoard.setCreateBy(ShiroUtils.getLoginName());

        kbBoard.setCreateTime(DateUtils.getNowDate());
        return kbBoardMapper.insertKbBoard(kbBoard);
    }

    /**
     * 修改任务看板
     * * @param kbBoard 任务看板
     * @return 结果
     */
    @Override
    public int updateKbBoard(KbBoard kbBoard)
    {
        // [建议] 设置更新者和更新时间
        kbBoard.setUpdateBy(ShiroUtils.getLoginName());
        kbBoard.setUpdateTime(DateUtils.getNowDate());
        return kbBoardMapper.updateKbBoard(kbBoard);
    }

    /**
     * 批量删除任务看板
     * * @param boardIds 需要删除的任务看板主键
     * @return 结果
     */
    @Override
    public int deleteKbBoardByBoardIds(String boardIds)
    {
        return kbBoardMapper.deleteKbBoardByBoardIds(Convert.toStrArray(boardIds));
    }

    /**
     * 删除任务看板信息
     * * @param boardId 任务看板主键
     * @return 结果
     */
    @Override
    public int deleteKbBoardByBoardId(Long boardId)
    {
        return kbBoardMapper.deleteKbBoardByBoardId(boardId);
    }
}