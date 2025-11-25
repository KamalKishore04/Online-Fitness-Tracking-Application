package com.fitnesstracker.model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Workout {

    private int id;
    private int userId;
    private LocalDate workoutDate;
    private String workoutType;
    private int durationMinutes;
    private String intensity; // LOW / MEDIUM / HIGH
    private String notes;
    private Timestamp createdAt;

    public Workout() {
    }

    public Workout(int userId, LocalDate workoutDate, String workoutType,
                   int durationMinutes, String intensity, String notes) {
        this.userId = userId;
        this.workoutDate = workoutDate;
        this.workoutType = workoutType;
        this.durationMinutes = durationMinutes;
        this.intensity = intensity;
        this.notes = notes;
    }

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

    public LocalDate getWorkoutDate() {
        return workoutDate;
    }
    public void setWorkoutDate(LocalDate workoutDate) {
        this.workoutDate = workoutDate;
    }

    public String getWorkoutType() {
        return workoutType;
    }
    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getIntensity() {
        return intensity;
    }
    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
