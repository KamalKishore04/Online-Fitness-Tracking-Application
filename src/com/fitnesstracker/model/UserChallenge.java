package com.fitnesstracker.model;

import java.sql.Timestamp;

public class UserChallenge {

    private int id;
    private int userId;
    private int challengeId;
    private String status; // JOINED / COMPLETED
    private Timestamp joinedAt;
    private Timestamp completedAt;

    public UserChallenge() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getChallengeId() {
        return challengeId;
    }
    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getJoinedAt() {
        return joinedAt;
    }
    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }
    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }
}
