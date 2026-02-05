package com.ruoyi.kanban.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 任务卡片对象 kb_card
 * @author ruoyi
 */
public class KbCard extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 卡片ID */
    private Long cardId;

    /** 归属清单ID */
    @Excel(name = "归属清单ID")
    private Long listId;

    /** 归属看板ID */
    @Excel(name = "归属看板ID")
    private Long boardId;

    /** 任务标题 */
    @Excel(name = "任务标题")
    private String cardTitle;

    /** 任务详情 */
    @Excel(name = "任务详情")
    private String cardContent;

    /** 优先级(字典kb_card_priority) */
    @Excel(name = "优先级")
    private String priority;

    /** 截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deadline;

    /** 卡片排序 */
    private Long orderNum;

    /** 状态（0进行中 1已完成 2已归档） */
    @Excel(name = "状态", readConverterExp = "0=进行中,1=已完成,2=已归档")
    private String status;

    /** 任务进度 (0-100) */
    @Excel(name = "任务进度")
    private Integer progress;

    /** 执行人ID */
    @Excel(name = "执行人ID")
    private Long executorId;

    /** 是否优秀任务 (Y/N) */
    @Excel(name = "是否优秀")
    private String isExcellent;

    /** 实际完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /** 定点发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    public void setCardId(Long cardId) { this.cardId = cardId; }
    public Long getCardId() { return cardId; }

    public void setListId(Long listId) { this.listId = listId; }
    public Long getListId() { return listId; }

    public void setBoardId(Long boardId) { this.boardId = boardId; }
    public Long getBoardId() { return boardId; }

    public void setCardTitle(String cardTitle) { this.cardTitle = cardTitle; }
    public String getCardTitle() { return cardTitle; }

    public void setCardContent(String cardContent) { this.cardContent = cardContent; }
    public String getCardContent() { return cardContent; }

    public void setPriority(String priority) { this.priority = priority; }
    public String getPriority() { return priority; }

    public void setDeadline(Date deadline) { this.deadline = deadline; }
    public Date getDeadline() { return deadline; }

    public void setOrderNum(Long orderNum) { this.orderNum = orderNum; }
    public Long getOrderNum() { return orderNum; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    public void setProgress(Integer progress) { this.progress = progress; }
    public Integer getProgress() { return progress; }

    public void setExecutorId(Long executorId) { this.executorId = executorId; }
    public Long getExecutorId() { return executorId; }

    public void setIsExcellent(String isExcellent) { this.isExcellent = isExcellent; }
    public String getIsExcellent() { return isExcellent; }

    public void setFinishTime(Date finishTime) { this.finishTime = finishTime; }
    public Date getFinishTime() { return finishTime; }

    public void setPublishTime(Date publishTime) { this.publishTime = publishTime; }
    public Date getPublishTime() { return publishTime; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("cardId", getCardId())
                .append("listId", getListId())
                .append("boardId", getBoardId())
                .append("cardTitle", getCardTitle())
                .append("status", getStatus())
                .append("progress", getProgress())
                .append("executorId", getExecutorId())
                .append("createTime", getCreateTime())
                .toString();
    }
}