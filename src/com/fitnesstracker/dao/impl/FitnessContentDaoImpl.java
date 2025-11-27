package com.fitnesstracker.dao.impl;

import com.fitnesstracker.dao.FitnessContentDao;
import com.fitnesstracker.model.FitnessContent;
import com.fitnesstracker.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FitnessContentDaoImpl implements FitnessContentDao {

    @Override
    public void create(FitnessContent content) throws SQLException {
        String sql = "INSERT INTO fitness_content " +
                "(title, content_type, description, content_body, status, submitted_by) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, content.getTitle());
            ps.setString(2, content.getContentType());
            ps.setString(3, content.getDescription());
            ps.setString(4, content.getContentBody());
            ps.setString(5, content.getStatus());
            ps.setInt(6, content.getSubmittedBy());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating fitness content failed, no rows affected.");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    content.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public void updateStatus(int id, String status, Integer reviewedBy) throws SQLException {
        String sql = "UPDATE fitness_content " +
                "SET status = ?, reviewed_by = ?, reviewed_at = CURRENT_TIMESTAMP " +
                "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            if (reviewedBy != null) {
                ps.setInt(2, reviewedBy);
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setInt(3, id);

            ps.executeUpdate();
        }
    }

    @Override
    public FitnessContent findById(int id) throws SQLException {
        String sql = "SELECT * FROM fitness_content WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<FitnessContent> findAll() throws SQLException {
        String sql = "SELECT * FROM fitness_content ORDER BY submitted_at DESC";
        List<FitnessContent> list = new ArrayList<>();

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
    public List<FitnessContent> findByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM fitness_content WHERE status = ? ORDER BY submitted_at DESC";
        List<FitnessContent> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<FitnessContent> findApproved() throws SQLException {
        return findByStatus("APPROVED");
    }

    @Override
    public List<FitnessContent> findByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM fitness_content WHERE submitted_by = ? ORDER BY submitted_at DESC";
        List<FitnessContent> list = new ArrayList<>();

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

    private FitnessContent mapRow(ResultSet rs) throws SQLException {
        FitnessContent c = new FitnessContent();
        c.setId(rs.getInt("id"));
        c.setTitle(rs.getString("title"));
        c.setContentType(rs.getString("content_type"));
        c.setDescription(rs.getString("description"));
        c.setContentBody(rs.getString("content_body"));
        c.setStatus(rs.getString("status"));
        c.setSubmittedBy(rs.getInt("submitted_by"));
        c.setReviewedBy((Integer) rs.getObject("reviewed_by"));
        c.setSubmittedAt(rs.getTimestamp("submitted_at"));
        c.setReviewedAt(rs.getTimestamp("reviewed_at"));
        return c;
    }
}
