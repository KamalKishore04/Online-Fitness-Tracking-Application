package com.fitnesstracker.dao.impl;

import com.fitnesstracker.dao.UserChallengeDao;
import com.fitnesstracker.model.UserChallenge;
import com.fitnesstracker.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserChallengeDaoImpl implements UserChallengeDao {

    @Override
    public void create(UserChallenge uc) throws SQLException {
        String sql = "INSERT INTO user_challenges (user_id, challenge_id, status) " +
                "VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, uc.getUserId());
            ps.setInt(2, uc.getChallengeId());
            ps.setString(3, uc.getStatus());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating user_challenge failed, no rows affected.");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    uc.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public boolean exists(int userId, int challengeId) throws SQLException {
        String sql = "SELECT 1 FROM user_challenges WHERE user_id = ? AND challenge_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, challengeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<UserChallenge> findByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM user_challenges WHERE user_id = ? ORDER BY joined_at DESC";
        List<UserChallenge> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }
        return result;
    }

    @Override
    public List<UserChallenge> findByUserAndStatus(int userId, String status) throws SQLException {
        String sql = "SELECT * FROM user_challenges WHERE user_id = ? AND status = ? ORDER BY joined_at DESC";
        List<UserChallenge> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }
        return result;
    }

    private UserChallenge mapRow(ResultSet rs) throws SQLException {
        UserChallenge uc = new UserChallenge();
        uc.setId(rs.getInt("id"));
        uc.setUserId(rs.getInt("user_id"));
        uc.setChallengeId(rs.getInt("challenge_id"));
        uc.setStatus(rs.getString("status"));
        uc.setJoinedAt(rs.getTimestamp("joined_at"));
        uc.setCompletedAt(rs.getTimestamp("completed_at"));
        return uc;
    }
}
