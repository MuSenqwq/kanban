package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.mapper.SysUserMessageMapper;
import com.ruoyi.system.domain.SysUserMessage;
import com.ruoyi.system.service.ISysUserMessageService;

/**
 * 用户消息Service业务层处理
 */
@Service
public class SysUserMessageServiceImpl implements ISysUserMessageService
{
    @Autowired
    private SysUserMessageMapper sysUserMessageMapper;

    /**
     * 查询用户消息列表
     */
    @Override
    public List<SysUserMessage> selectSysUserMessageList(SysUserMessage sysUserMessage)
    {
        return sysUserMessageMapper.selectSysUserMessageList(sysUserMessage);
    }

    /**
     * 新增用户消息
     */
    @Override
    public int insertSysUserMessage(SysUserMessage sysUserMessage)
    {
        sysUserMessage.setCreateTime(DateUtils.getNowDate());
        // 默认状态处理
        if (sysUserMessage.getIsRead() == null) {
            sysUserMessage.setIsRead("0"); // 默认未读
        }
        if (sysUserMessage.getType() == null) {
            sysUserMessage.setType("2"); // 默认任务消息
        }
        return sysUserMessageMapper.insertSysUserMessage(sysUserMessage);
    }

    /**
     * 修改用户消息（如：标记为已读）
     */
    @Override
    public int updateSysUserMessage(SysUserMessage sysUserMessage)
    {
        return sysUserMessageMapper.updateSysUserMessage(sysUserMessage);
    }

    /**
     * 统计未读消息数
     */
    @Override
    public int countUnreadMessages(Long userId)
    {
        return sysUserMessageMapper.countUnreadMessages(userId);
    }

    /**
     * 发送任务类消息（快捷方法）
     */
    @Override
    public int sendTaskMessage(Long toUserId, String title, String content)
    {
        SysUserMessage msg = new SysUserMessage();
        msg.setUserId(toUserId);
        msg.setTitle(title);
        msg.setContent(content);
        msg.setType("2"); // 2代表任务消息
        msg.setIsRead("0"); // 0代表未读
        return insertSysUserMessage(msg);
    }
}