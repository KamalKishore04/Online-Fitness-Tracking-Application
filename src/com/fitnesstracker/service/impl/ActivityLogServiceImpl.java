package com.fitnesstracker.service.impl;

import com.fitnesstracker.dao.ActivityLogDao;
import com.fitnesstracker.dao.impl.ActivityLogDaoImpl;
import com.fitnesstracker.model.ActivityLog;
import com.fitnesstracker.service.ActivityLogService;

import java.time.LocalDateTime;
import java.util.List;

public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogDao dao;

    public ActivityLogServiceImpl() {
        this.dao = new ActivityLogDaoImpl();
    }

    @Override
    public void log(Integer userId, String action) throws Exception {
        if (action == null || action.isBlank()) return;
        ActivityLog log = new ActivityLog();
        log.setUserId(userId);
        log.setAction(action);
        dao.log(log);
    }

    @Override
    public List<ActivityLog> getAll() throws Exception {
        return dao.findAll();
    }

    @Override
    public List<ActivityLog> getByUser(Integer userId) throws Exception {
        return dao.findByUser(userId);
    }

    @Override
    public List<ActivityLog> getByDateRange(LocalDateTime from, LocalDateTime to) throws Exception {
        return dao.findByDateRange(from, to);
    }
}
