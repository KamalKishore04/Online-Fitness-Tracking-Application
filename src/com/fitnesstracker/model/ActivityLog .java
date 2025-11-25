package com.fitnesstracker.model;

import java.sql.Timestamp;

public class ActivityLog {

    private int id;
    private Integer userId;
    private String action;
    private Timestamp actionTime;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    public Timestamp getActionTime() {
        return actionTime;
    }
    public void setActionTime(Timestamp actionTime) {
        this.actionTime = actionTime;
    }
}
