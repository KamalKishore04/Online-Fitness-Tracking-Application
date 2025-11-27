package com.fitnesstracker.dao.impl;

import com.fitnesstracker.dao.ActivityLogDao;
import com.fitnesstracker.model.ActivityLog;
import com.fitnesstracker.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDaoImpl implements ActivityLogDao {

    @Override
    public void log(ActivityLog log) throws SQLException {
        String sql = "INSERT INTO activity_log (user_id, action) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (log.getUserId() != null) {
                ps.setInt(1, log.getUserId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, log.getAction());
            ps.executeUpdate();
        }
    }

    @Override
    public List<ActivityLog> findAll() throws SQLException {
        String sql = "SELECT * FROM activity_log ORDER BY action_time DESC";
        List<ActivityLog> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<ActivityLog> findByUser(Integer userId) throws SQLException {
        String sql = "SELECT * FROM activity_log WHERE user_id = ? ORDER BY action_time DESC";
        List<ActivityLog> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<ActivityLog> findByDateRange(LocalDateTime from, LocalDateTime to) throws SQLException {
        String sql = "SELECT * FROM activity_log WHERE action_time BETWEEN ? AND ? ORDER BY action_time DESC";
        List<ActivityLog> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(from));
            ps.setTimestamp(2, Timestamp.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    private ActivityLog mapRow(ResultSet rs) throws SQLException {
        ActivityLog log = new ActivityLog();
        log.setId(rs.getInt("id"));
        log.setUserId((Integer) rs.getObject("user_id"));
        log.setAction(rs.getString("action"));
        log.setActionTime(rs.getTimestamp("action_time"));
        return log;
    }
}
