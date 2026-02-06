package com.ruoyi.kanban.service;

import java.util.List;
import com.ruoyi.kanban.domain.KbTeam;

/**
 * 团队信息Service接口
 * * @author ruoyi
 * @date 2025-02-06
 */
public interface IKbTeamService
{
    /**
     * 查询团队信息
     * * @param teamId 团队信息主键
     * @return 团队信息
     */
    public KbTeam selectKbTeamByTeamId(Long teamId);

    /**
     * 查询团队信息列表
     * * @param kbTeam 团队信息
     * @return 团队信息集合
     */
    public List<KbTeam> selectKbTeamList(KbTeam kbTeam);

    /**
     * 新增团队信息
     * * @param kbTeam 团队信息
     * @return 结果
     */
    public int insertKbTeam(KbTeam kbTeam);

    /**
     * 修改团队信息
     * * @param kbTeam 团队信息
     * @return 结果
     */
    public int updateKbTeam(KbTeam kbTeam);

    /**
     * 批量删除团队信息
     * * @param teamIds 需要删除的团队信息主键集合
     * @return 结果
     */
    public int deleteKbTeamByTeamIds(String teamIds);

    /**
     * 删除团队信息信息
     * * @param teamId 团队信息主键
     * @return 结果
     */
    public int deleteKbTeamByTeamId(Long teamId);

    /**
     * 根据团队ID查询关联的用户ID列表
     * * @param teamId 团队ID
     * @return 用户ID列表
     */
    public List<Long> selectUserIdsByTeamId(Long teamId);
}