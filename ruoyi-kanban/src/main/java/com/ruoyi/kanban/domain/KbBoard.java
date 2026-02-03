package com.ruoyi.kanban.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 任务看板对象 kb_board
 * 
 * @author ruoyi
 * @date 2026-02-03
 */
public class KbBoard extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 看板ID */
    private Long boardId;

    /** 负责人ID(关联sys_user) */
    @Excel(name = "负责人ID(关联sys_user)")
    private Long userId;

    /** 看板名称 */
    @Excel(name = "看板名称")
    private String boardName;

    /** 看板简介 */
    @Excel(name = "看板简介")
    private String intro;

    /** 主题颜色 */
    @Excel(name = "主题颜色")
    private String themeColor;

    /** 状态（0正常 1关闭） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=关闭")
    private String status;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setBoardId(Long boardId) 
    {
        this.boardId = boardId;
    }

    public Long getBoardId() 
    {
        return boardId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setBoardName(String boardName) 
    {
        this.boardName = boardName;
    }

    public String getBoardName() 
    {
        return boardName;
    }

    public void setIntro(String intro) 
    {
        this.intro = intro;
    }

    public String getIntro() 
    {
        return intro;
    }

    public void setThemeColor(String themeColor) 
    {
        this.themeColor = themeColor;
    }

    public String getThemeColor() 
    {
        return themeColor;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setOrderNum(Integer orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() 
    {
        return orderNum;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("boardId", getBoardId())
            .append("userId", getUserId())
            .append("boardName", getBoardName())
            .append("intro", getIntro())
            .append("themeColor", getThemeColor())
            .append("status", getStatus())
            .append("orderNum", getOrderNum())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
