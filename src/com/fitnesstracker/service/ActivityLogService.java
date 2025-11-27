package com.fitnesstracker.service;

import com.fitnesstracker.model.ActivityLog;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityLogService {

    void log(Integer userId, String action) throws Exception;

    List<ActivityLog> getAll() throws Exception;

    List<ActivityLog> getByUser(Integer userId) throws Exception;

    List<ActivityLog> getByDateRange(LocalDateTime from, LocalDateTime to) throws Exception;
}
