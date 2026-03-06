package com.ruoyi.kanban.domain;
import java.util.Date;
    public class SysMessageVO {
        /** 消息ID */
        private Long messageId;
        /** 消息标题 */
        private String title;
        /** 消息内容 */
        private String content;
        /** 消息类型：1系统 2任务 */
        private String type;
        /** 创建时间（发送时间） */
        private Date createTime;
        /** 接收人名称 */
        private String receiveUserName;

        public SysMessageVO(Long messageId, String title, String content, String type, Date createTime, String receiveUserName) {
            this.messageId = messageId;
            this.title = title;
            this.content = content;
            this.type = type;
            this.createTime = createTime;
            this.receiveUserName = receiveUserName;
        }

        public Long getMessageId() { return messageId; }
        public void setMessageId(Long messageId) { this.messageId = messageId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Date getCreateTime() { return createTime; }
        public void setCreateTime(Date createTime) { this.createTime = createTime; }
        public String getReceiveUserName() { return receiveUserName; }
        public void setReceiveUserName(String receiveUserName) { this.receiveUserName = receiveUserName; }
    }

