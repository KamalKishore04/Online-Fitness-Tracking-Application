package com.fitnesstracker.service.impl;

import com.fitnesstracker.model.User;
import com.fitnesstracker.service.ProfileService;
import com.fitnesstracker.util.DBConnection;

import java.sql.*;

public class ProfileServiceImpl implements ProfileService {

    @Override
    public User getUserById(int userId) throws Exception {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setName(rs.getString("name"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                    u.setRole(rs.getString("role"));
                    u.setCreatedAt(rs.getTimestamp("created_at"));
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public void updateProfile(int userId, String name, String email) throws Exception {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, userId);
            ps.executeUpdate();
        }
    }

    @Override
    public void changePassword(int userId, String oldPassword, String newPassword) throws Exception {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }

        String sql = "SELECT password FROM users WHERE id = ?";
        String current;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new Exception("User not found");
                }
                current = rs.getString("password");
            }
        }

        if (!current.equals(oldPassword)) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        String updateSql = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }
}
