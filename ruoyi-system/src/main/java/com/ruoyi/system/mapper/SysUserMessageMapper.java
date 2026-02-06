package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysUserMessage;

/**
 * 用户消息Mapper接口
 */
public interface SysUserMessageMapper
{
    /**
     * 查询用户消息列表
     */
    public List<SysUserMessage> selectSysUserMessageList(SysUserMessage sysUserMessage);

    /**
     * 新增用户消息
     */
    public int insertSysUserMessage(SysUserMessage sysUserMessage);

    /**
     * 修改用户消息
     */
    public int updateSysUserMessage(SysUserMessage sysUserMessage);

    /**
     * 统计未读消息数
     */
    public int countUnreadMessages(Long userId);
}