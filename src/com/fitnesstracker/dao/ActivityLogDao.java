package com.fitnesstracker.dao;

import com.fitnesstracker.model.ActivityLog;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface ActivityLogDao {

    void log(ActivityLog log) throws SQLException;

    List<ActivityLog> findAll() throws SQLException;

    List<ActivityLog> findByUser(Integer userId) throws SQLException;

    List<ActivityLog> findByDateRange(LocalDateTime from, LocalDateTime to) throws SQLException;
}
