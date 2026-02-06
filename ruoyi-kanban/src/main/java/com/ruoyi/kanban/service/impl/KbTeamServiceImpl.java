package com.ruoyi.kanban.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.kanban.mapper.KbTeamMapper;
import com.ruoyi.kanban.domain.KbTeam;
import com.ruoyi.kanban.service.IKbTeamService;
import com.ruoyi.common.core.text.Convert;

/**
 * 团队信息Service业务层处理
 * * @author ruoyi
 * @date 2025-02-06
 */
@Service
public class KbTeamServiceImpl implements IKbTeamService
{
    @Autowired
    private KbTeamMapper kbTeamMapper;

    /**
     * 查询团队信息
     * * @param teamId 团队信息主键
     * @return 团队信息
     */
    @Override
    public KbTeam selectKbTeamByTeamId(Long teamId)
    {
        return kbTeamMapper.selectKbTeamByTeamId(teamId);
    }

    /**
     * 查询团队信息列表
     * * @param kbTeam 团队信息
     * @return 团队信息
     */
    @Override
    public List<KbTeam> selectKbTeamList(KbTeam kbTeam)
    {
        return kbTeamMapper.selectKbTeamList(kbTeam);
    }

    /**
     * 新增团队信息
     * * @param kbTeam 团队信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertKbTeam(KbTeam kbTeam)
    {
        kbTeam.setCreateTime(DateUtils.getNowDate());
        int rows = kbTeamMapper.insertKbTeam(kbTeam);
        insertTeamMembers(kbTeam);
        return rows;
    }

    /**
     * 修改团队信息
     * * @param kbTeam 团队信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateKbTeam(KbTeam kbTeam)
    {
        kbTeam.setUpdateTime(DateUtils.getNowDate());
        int rows = kbTeamMapper.updateKbTeam(kbTeam);
        kbTeamMapper.deleteTeamMembersByTeamId(kbTeam.getTeamId());
        insertTeamMembers(kbTeam);
        return rows;
    }

    /**
     * 批量删除团队信息
     * * @param teamIds 需要删除的团队信息主键
     * @return 结果
     */
    @Override
    public int deleteKbTeamByTeamIds(String teamIds)
    {
        return kbTeamMapper.deleteKbTeamByTeamIds(Convert.toStrArray(teamIds));
    }

    /**
     * 删除团队信息信息
     * * @param teamId 团队信息主键
     * @return 结果
     */
    @Override
    public int deleteKbTeamByTeamId(Long teamId)
    {
        return kbTeamMapper.deleteKbTeamByTeamId(teamId);
    }

    /**
     * 根据团队ID查询关联的用户ID列表
     */
    @Override
    public List<Long> selectUserIdsByTeamId(Long teamId)
    {
        return kbTeamMapper.selectUserIdsByTeamId(teamId);
    }

    /**
     * 新增团队成员关联信息
     */
    public void insertTeamMembers(KbTeam kbTeam)
    {
        Long[] userIds = kbTeam.getUserIds();
        if (StringUtils.isNotNull(userIds) && userIds.length > 0)
        {
            kbTeamMapper.batchInsertTeamMembers(kbTeam.getTeamId(), userIds);
        }
    }
}