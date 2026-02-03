package com.ruoyi.kanban.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 看板任务列对象 kb_list
 * * @author ruoyi
 * @date 2026-02-03
 */
public class KbList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 清单ID */
    private Long listId;

    /** 归属看板ID */
    @Excel(name = "归属看板ID")
    private Long boardId;

    /** 清单名称(如:进行中) */
    @Excel(name = "清单名称(如:进行中)")
    private String listName;

    /** 状态（0正常 1归档） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=归档")
    private String listStatus;

    /** 列排序(拖拽用) */
    @Excel(name = "列排序(拖拽用)")
    private Long orderNum;

    /** * 业务字段：该列下的卡片列表
     * (不存数据库，仅用于前端展示)
     */
    private List<KbCard> cards;

    public void setListId(Long listId)
    {
        this.listId = listId;
    }

    public Long getListId()
    {
        return listId;
    }

    public void setBoardId(Long boardId)
    {
        this.boardId = boardId;
    }

    public Long getBoardId()
    {
        return boardId;
    }

    public void setListName(String listName)
    {
        this.listName = listName;
    }

    public String getListName()
    {
        return listName;
    }

    public void setListStatus(String listStatus)
    {
        this.listStatus = listStatus;
    }

    public String getListStatus()
    {
        return listStatus;
    }

    public void setOrderNum(Long orderNum)
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum()
    {
        return orderNum;
    }

    public List<KbCard> getCards() {
        return cards;
    }

    public void setCards(List<KbCard> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("listId", getListId())
                .append("boardId", getBoardId())
                .append("listName", getListName())
                .append("listStatus", getListStatus())
                .append("orderNum", getOrderNum())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("cards", getCards())
                .toString();
    }
}