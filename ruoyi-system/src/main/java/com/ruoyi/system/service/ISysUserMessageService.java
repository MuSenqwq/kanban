package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysUserMessage;

/**
 * 用户消息Service接口
 */
public interface ISysUserMessageService
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

    /**
     * 发送任务类消息（快捷方法）
     */
    public int sendTaskMessage(Long toUserId, String title, String content);

    int batchInsert(List<SysUserMessage> messageList);
}