package com.fitnesstracker.service.impl;

import com.fitnesstracker.dao.FitnessContentDao;
import com.fitnesstracker.dao.impl.FitnessContentDaoImpl;
import com.fitnesstracker.logging.ActivityLogger;
import com.fitnesstracker.model.FitnessContent;
import com.fitnesstracker.service.FitnessContentService;

import java.util.List;

public class FitnessContentServiceImpl implements FitnessContentService {

    private final FitnessContentDao dao;

    public FitnessContentServiceImpl() {
        this.dao = new FitnessContentDaoImpl();
    }

    @Override
    public void submitContent(FitnessContent content, int userId) throws Exception {
        if (content.getTitle() == null || content.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (content.getContentType() == null || content.getContentType().isBlank()) {
            throw new IllegalArgumentException("Content type is required");
        }
        if (content.getContentBody() == null || content.getContentBody().isBlank()) {
            throw new IllegalArgumentException("Content body is required");
        }
        content.setSubmittedBy(userId);
        content.setStatus("PENDING");
        dao.create(content);

        // LOG
        ActivityLogger.log(userId,
                "Submitted fitness content id=" + content.getId() +
                        ", title=\"" + content.getTitle() + "\"");
    }

    @Override
    public void approveContent(int contentId, int adminId) throws Exception {
        if (contentId <= 0) throw new IllegalArgumentException("Invalid content id");
        dao.updateStatus(contentId, "APPROVED", adminId);
        ActivityLogger.log(adminId, "Approved fitness content id=" + contentId);
    }

    @Override
    public void rejectContent(int contentId, int adminId) throws Exception {
        if (contentId <= 0) throw new IllegalArgumentException("Invalid content id");
        dao.updateStatus(contentId, "REJECTED", adminId);
        ActivityLogger.log(adminId, "Rejected fitness content id=" + contentId);
    }

    @Override
    public List<FitnessContent> getPendingContent() throws Exception {
        return dao.findByStatus("PENDING");
    }

    @Override
    public List<FitnessContent> getApprovedContent() throws Exception {
        return dao.findApproved();
    }

    @Override
    public List<FitnessContent> getAllContent() throws Exception {
        return dao.findAll();
    }

    @Override
    public List<FitnessContent> getUserContent(int userId) throws Exception {
        if (userId <= 0) throw new IllegalArgumentException("Invalid user id");
        return dao.findByUser(userId);
    }

    @Override
    public FitnessContent getById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid content id");
        return dao.findById(id);
    }
}
