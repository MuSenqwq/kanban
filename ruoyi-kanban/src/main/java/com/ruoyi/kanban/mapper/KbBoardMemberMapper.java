package com.ruoyi.kanban.mapper;

import java.util.List;
import com.ruoyi.kanban.domain.KbBoardMember;

/**
 * 看板成员Mapper接口
 */
public interface KbBoardMemberMapper
{
    /**
     * 查询看板成员列表（包含关联的用户信息）
     */
    public List<KbBoardMember> selectKbBoardMemberList(KbBoardMember kbBoardMember);

    /**
     * 新增看板成员
     */
    public int insertKbBoardMember(KbBoardMember kbBoardMember);

    /**
     * 删除看板成员
     */
    public int deleteKbBoardMemberById(Long id);

    /**
     * 检查是否已存在
     */
    public int checkMemberUnique(KbBoardMember kbBoardMember);
}