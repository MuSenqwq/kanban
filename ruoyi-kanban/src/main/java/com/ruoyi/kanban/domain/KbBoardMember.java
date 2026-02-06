package com.ruoyi.kanban.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 看板成员对象 kb_board_member
 */
public class KbBoardMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 看板ID */
    @Excel(name = "看板ID")
    private Long boardId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 角色（0管理员 1普通成员） */
    @Excel(name = "角色", readConverterExp = "0=管理员,1=普通成员")
    private String role;

    /** 用户名（非数据库字段，用于显示） */
    private String userName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
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
    public void setRole(String role)
    {
        this.role = role;
    }

    public String getRole()
    {
        return role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("boardId", getBoardId())
                .append("userId", getUserId())
                .append("role", getRole())
                .append("createTime", getCreateTime())
                .toString();
    }
}