package com.ruoyi.kanban.mapper;

import java.util.List;
import com.ruoyi.kanban.domain.KbTeam;
import org.apache.ibatis.annotations.Param;

/**
 * 团队信息Mapper接口
 * 
 * @author ruoyi
 * @date 2026-02-06
 */
public interface KbTeamMapper 
{
    /**
     * 查询团队信息
     * 
     * @param teamId 团队信息主键
     * @return 团队信息
     */
    public KbTeam selectKbTeamByTeamId(Long teamId);

    /**
     * 查询团队信息列表
     * 
     * @param kbTeam 团队信息
     * @return 团队信息集合
     */
    public List<KbTeam> selectKbTeamList(KbTeam kbTeam);

    /**
     * 新增团队信息
     * 
     * @param kbTeam 团队信息
     * @return 结果
     */
    public int insertKbTeam(KbTeam kbTeam);

    /**
     * 修改团队信息
     * 
     * @param kbTeam 团队信息
     * @return 结果
     */
    public int updateKbTeam(KbTeam kbTeam);

    /**
     * 删除团队信息
     * 
     * @param teamId 团队信息主键
     * @return 结果
     */
    public int deleteKbTeamByTeamId(Long teamId);

    /**
     * 批量删除团队信息
     * 
     * @param teamIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKbTeamByTeamIds(String[] teamIds);
    // ... 生成的方法

    /**
     * 批量新增团队成员
     */
    public int batchInsertTeamMembers(@Param("teamId") Long teamId, @Param("userIds") Long[] userIds);

    /**
     * 删除团队所有成员关联
     */
    public int deleteTeamMembersByTeamId(Long teamId);

    /**
     * 根据团队ID查询关联的用户ID列表
     */
    public List<Long> selectUserIdsByTeamId(Long teamId);

}
