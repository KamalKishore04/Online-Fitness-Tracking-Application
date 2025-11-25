package com.fitnesstracker.model;

import java.sql.Timestamp;

public class FitnessContent {

    private int id;
    private String title;
    private String contentType;
    private String description;
    private String contentBody;
    private String status;       // PENDING / APPROVED / REJECTED
    private int submittedBy;
    private Integer reviewedBy;  // nullable
    private Timestamp submittedAt;
    private Timestamp reviewedAt;

    public FitnessContent() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentBody() {
        return contentBody;
    }
    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public int getSubmittedBy() {
        return submittedBy;
    }
    public void setSubmittedBy(int submittedBy) {
        this.submittedBy = submittedBy;
    }

    public Integer getReviewedBy() {
        return reviewedBy;
    }
    public void setReviewedBy(Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }
    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Timestamp getReviewedAt() {
        return reviewedAt;
    }
    public void setReviewedAt(Timestamp reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}

