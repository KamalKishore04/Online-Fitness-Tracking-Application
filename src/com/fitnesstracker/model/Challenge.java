package com.fitnesstracker.model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Challenge {

    private int id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String difficulty; // EASY / MEDIUM / HARD
    private Integer targetWorkouts; // optional
    private Integer targetMinutes;  // optional
    private int createdBy;
    private Timestamp createdAt;

    public Challenge() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getTargetWorkouts() {
        return targetWorkouts;
    }
    public void setTargetWorkouts(Integer targetWorkouts) {
        this.targetWorkouts = targetWorkouts;
    }

    public Integer getTargetMinutes() {
        return targetMinutes;
    }
    public void setTargetMinutes(Integer targetMinutes) {
        this.targetMinutes = targetMinutes;
    }

    public int getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
