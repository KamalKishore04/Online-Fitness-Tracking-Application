package com.fitnesstracker.dao.impl;

import com.fitnesstracker.dao.WorkoutDao;
import com.fitnesstracker.model.Workout;
import com.fitnesstracker.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDaoImpl implements WorkoutDao {

    @Override
    public void createWorkout(Workout workout) throws SQLException {
        // NOTE: column name is `type` in DB
        String sql = "INSERT INTO workouts " +
                "(user_id, workout_date, type, duration_minutes, intensity, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, workout.getUserId());
            ps.setDate(2, Date.valueOf(workout.getWorkoutDate()));
            ps.setString(3, workout.getWorkoutType());      // maps to column `type`
            ps.setInt(4, workout.getDurationMinutes());
            ps.setString(5, workout.getIntensity());
            ps.setString(6, workout.getNotes());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating workout failed, no rows affected.");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    workout.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public void updateWorkout(Workout workout) throws SQLException {
        String sql = "UPDATE workouts SET workout_date = ?, type = ?, " +
                "duration_minutes = ?, intensity = ?, notes = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(workout.getWorkoutDate()));
            ps.setString(2, workout.getWorkoutType());      // column `type`
            ps.setInt(3, workout.getDurationMinutes());
            ps.setString(4, workout.getIntensity());
            ps.setString(5, workout.getNotes());
            ps.setInt(6, workout.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void deleteWorkout(int id) throws SQLException {
        String sql = "DELETE FROM workouts WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Workout findById(int id) throws SQLException {
        String sql = "SELECT * FROM workouts WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToWorkout(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Workout> findByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM workouts WHERE user_id = ? " +
                "ORDER BY workout_date DESC, created_at DESC";

        List<Workout> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToWorkout(rs));
                }
            }
        }
        return result;
    }

    @Override
    public List<Workout> findByUserAndDateRange(int userId, LocalDate from, LocalDate to) throws SQLException {
        String sql = "SELECT * FROM workouts WHERE user_id = ? " +
                "AND workout_date BETWEEN ? AND ? " +
                "ORDER BY workout_date DESC";

        List<Workout> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToWorkout(rs));
                }
            }
        }
        return result;
    }

    private Workout mapRowToWorkout(ResultSet rs) throws SQLException {
        Workout w = new Workout();
        w.setId(rs.getInt("id"));
        w.setUserId(rs.getInt("user_id"));
        w.setWorkoutDate(rs.getDate("workout_date").toLocalDate());
        // NOTE: column name is `type`
        w.setWorkoutType(rs.getString("type"));
        w.setDurationMinutes(rs.getInt("duration_minutes"));
        w.setIntensity(rs.getString("intensity"));
        w.setNotes(rs.getString("notes"));
        w.setCreatedAt(rs.getTimestamp("created_at"));
        return w;
    }
}
