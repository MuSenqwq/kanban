package com.ruoyi.kanban.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.kanban.mapper.KbListMapper;
import com.ruoyi.kanban.domain.KbList;
import com.ruoyi.kanban.service.IKbListService;
import com.ruoyi.common.core.text.Convert;

/**
 * 看板任务列Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-03
 */
@Service
public class KbListServiceImpl implements IKbListService 
{
    @Autowired
    private KbListMapper kbListMapper;

    /**
     * 查询看板任务列
     * 
     * @param listId 看板任务列主键
     * @return 看板任务列
     */
    @Override
    public KbList selectKbListByListId(Long listId)
    {
        return kbListMapper.selectKbListByListId(listId);
    }

    /**
     * 查询看板任务列列表
     * 
     * @param kbList 看板任务列
     * @return 看板任务列
     */
    @Override
    public List<KbList> selectKbListList(KbList kbList)
    {
        return kbListMapper.selectKbListList(kbList);
    }

    /**
     * 新增看板任务列
     * 
     * @param kbList 看板任务列
     * @return 结果
     */
    @Override
    public int insertKbList(KbList kbList)
    {
        kbList.setCreateTime(DateUtils.getNowDate());
        return kbListMapper.insertKbList(kbList);
    }

    /**
     * 修改看板任务列
     * 
     * @param kbList 看板任务列
     * @return 结果
     */
    @Override
    public int updateKbList(KbList kbList)
    {
        kbList.setUpdateTime(DateUtils.getNowDate());
        return kbListMapper.updateKbList(kbList);
    }

    /**
     * 批量删除看板任务列
     * 
     * @param listIds 需要删除的看板任务列主键
     * @return 结果
     */
    @Override
    public int deleteKbListByListIds(String listIds)
    {
        return kbListMapper.deleteKbListByListIds(Convert.toStrArray(listIds));
    }

    /**
     * 删除看板任务列信息
     * 
     * @param listId 看板任务列主键
     * @return 结果
     */
    @Override
    public int deleteKbListByListId(Long listId)
    {
        return kbListMapper.deleteKbListByListId(listId);
    }
}
