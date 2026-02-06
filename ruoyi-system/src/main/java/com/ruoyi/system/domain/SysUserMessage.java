package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户消息对象 sys_user_message
 */
public class SysUserMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    private Long messageId;

    /** 接收人ID */
    @Excel(name = "接收人ID")
    private Long userId;

    /** 消息标题 */
    @Excel(name = "消息标题")
    private String title;

    /** 消息内容 */
    @Excel(name = "消息内容")
    private String content;

    /** 类型: 1系统 2任务 */
    @Excel(name = "类型: 1系统 2任务")
    private String type;

    /** 状态: 0未读 1已读 */
    @Excel(name = "状态: 0未读 1已读")
    private String isRead;

    public void setMessageId(Long messageId) { this.messageId = messageId; }
    public Long getMessageId() { return messageId; }

    public void setUserId(Long userId) { this.userId = userId; }
    public Long getUserId() { return userId; }

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return title; }

    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }

    public void setType(String type) { this.type = type; }
    public String getType() { return type; }

    public void setIsRead(String isRead) { this.isRead = isRead; }
    public String getIsRead() { return isRead; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("messageId", getMessageId())
                .append("userId", getUserId())
                .append("title", getTitle())
                .append("content", getContent())
                .append("type", getType())
                .append("isRead", getIsRead())
                .append("createTime", getCreateTime())
                .toString();
    }
}