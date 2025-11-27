package com.fitnesstracker.service;

import com.fitnesstracker.model.FitnessContent;

import java.util.List;

public interface FitnessContentService {

    void submitContent(FitnessContent content, int userId) throws Exception;

    void approveContent(int contentId, int adminId) throws Exception;

    void rejectContent(int contentId, int adminId) throws Exception;

    List<FitnessContent> getPendingContent() throws Exception;

    List<FitnessContent> getApprovedContent() throws Exception;

    List<FitnessContent> getAllContent() throws Exception;

    List<FitnessContent> getUserContent(int userId) throws Exception;

    FitnessContent getById(int id) throws Exception;
}

