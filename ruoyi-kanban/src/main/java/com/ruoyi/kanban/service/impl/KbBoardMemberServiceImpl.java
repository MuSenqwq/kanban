package com.ruoyi.kanban.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.kanban.mapper.KbBoardMemberMapper;
import com.ruoyi.kanban.domain.KbBoardMember;
import com.ruoyi.kanban.service.IKbBoardMemberService;

/**
 * 看板成员Service业务层处理
 */
@Service
public class KbBoardMemberServiceImpl implements IKbBoardMemberService
{
    @Autowired
    private KbBoardMemberMapper kbBoardMemberMapper;

    /**
     * 查询看板成员列表
     */
    @Override
    public List<KbBoardMember> selectKbBoardMemberList(KbBoardMember kbBoardMember)
    {
        return kbBoardMemberMapper.selectKbBoardMemberList(kbBoardMember);
    }

    /**
     * 新增看板成员
     */
    @Override
    public int insertKbBoardMember(KbBoardMember kbBoardMember)
    {
        // 简单查重：防止同一个用户重复加入同一个看板
        int count = kbBoardMemberMapper.checkMemberUnique(kbBoardMember);
        if (count > 0) {
            throw new ServiceException("该用户已在看板中");
        }
        kbBoardMember.setCreateTime(DateUtils.getNowDate());
        return kbBoardMemberMapper.insertKbBoardMember(kbBoardMember);
    }

    /**
     * 删除看板成员信息
     */
    @Override
    public int deleteKbBoardMemberById(Long id)
    {
        return kbBoardMemberMapper.deleteKbBoardMemberById(id);
    }
}