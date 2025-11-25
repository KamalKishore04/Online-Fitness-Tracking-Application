package com.fitnesstracker.dao.impl;

import com.fitnesstracker.dao.ChallengeDao;
import com.fitnesstracker.model.Challenge;
import com.fitnesstracker.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChallengeDaoImpl implements ChallengeDao {

    @Override
    public void create(Challenge c) throws SQLException {
        String sql = "INSERT INTO challenges " +
                "(name, description, start_date, end_date, difficulty, " +
                " target_workouts, target_minutes, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setDate(3, Date.valueOf(c.getStartDate()));
            ps.setDate(4, Date.valueOf(c.getEndDate()));
            ps.setString(5, c.getDifficulty());

            if (c.getTargetWorkouts() != null) {
                ps.setInt(6, c.getTargetWorkouts());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            if (c.getTargetMinutes() != null) {
                ps.setInt(7, c.getTargetMinutes());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            ps.setInt(8, c.getCreatedBy());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating challenge failed, no rows affected.");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Challenge c) throws SQLException {
        String sql = "UPDATE challenges SET name = ?, description = ?, " +
                "start_date = ?, end_date = ?, difficulty = ?, " +
                "target_workouts = ?, target_minutes = ? " +
                "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setDate(3, Date.valueOf(c.getStartDate()));
            ps.setDate(4, Date.valueOf(c.getEndDate()));
            ps.setString(5, c.getDifficulty());

            if (c.getTargetWorkouts() != null) {
                ps.setInt(6, c.getTargetWorkouts());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            if (c.getTargetMinutes() != null) {
                ps.setInt(7, c.getTargetMinutes());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            ps.setInt(8, c.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM challenges WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Challenge findById(int id) throws SQLException {
        String sql = "SELECT * FROM challenges WHERE id = ?";

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
    public List<Challenge> findAll() throws SQLException {
        String sql = "SELECT * FROM challenges " +
                "ORDER BY start_date DESC, created_at DESC";

        List<Challenge> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }
        return result;
    }

    @Override
    public List<Challenge> findActive(LocalDate today) throws SQLException {
        String sql = "SELECT * FROM challenges " +
                "WHERE start_date <= ? AND end_date >= ? " +
                "ORDER BY start_date ASC";

        List<Challenge> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Date d = Date.valueOf(today);
            ps.setDate(1, d);
            ps.setDate(2, d);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }
        return result;
    }

    private Challenge mapRow(ResultSet rs) throws SQLException {
        Challenge c = new Challenge();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        c.setStartDate(rs.getDate("start_date").toLocalDate());
        c.setEndDate(rs.getDate("end_date").toLocalDate());
        c.setDifficulty(rs.getString("difficulty"));

        Integer tw = (Integer) rs.getObject("target_workouts");
        Integer tm = (Integer) rs.getObject("target_minutes");
        c.setTargetWorkouts(tw);
        c.setTargetMinutes(tm);

        c.setCreatedBy(rs.getInt("created_by"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
