package com.ruoyi.kanban.service;

import java.util.List;
import com.ruoyi.kanban.domain.KbBoardMember;

/**
 * 看板成员Service接口
 */
public interface IKbBoardMemberService
{
    /**
     * 查询看板成员列表
     * * @param kbBoardMember 看板成员
     * @return 看板成员集合
     */
    public List<KbBoardMember> selectKbBoardMemberList(KbBoardMember kbBoardMember);

    /**
     * 新增看板成员
     * * @param kbBoardMember 看板成员
     * @return 结果
     */
    public int insertKbBoardMember(KbBoardMember kbBoardMember);

    /**
     * 删除看板成员信息
     * * @param id 看板成员ID
     * @return 结果
     */
    public int deleteKbBoardMemberById(Long id);
}