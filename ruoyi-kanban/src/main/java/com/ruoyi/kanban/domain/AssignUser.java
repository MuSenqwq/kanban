package com.ruoyi.kanban.domain;

/**
 * 指派专用用户实体
 */
public class AssignUser {
    private Long userId;   // 用户ID
    private String userName; // 用户名

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "AssignUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}