package com.fitnesstracker.dao.impl;

import com.fitnesstracker.dao.SystemSettingDao;
import com.fitnesstracker.model.SystemSetting;
import com.fitnesstracker.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemSettingDaoImpl implements SystemSettingDao {

    @Override
    public List<SystemSetting> findAll() throws SQLException {
        String sql = "SELECT * FROM system_settings ORDER BY setting_key ASC";
        List<SystemSetting> list = new ArrayList<>();

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
    public SystemSetting findByKey(String key) throws SQLException {
        String sql = "SELECT * FROM system_settings WHERE setting_key = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void updateValue(String key, String value) throws SQLException {
        String sql = "UPDATE system_settings SET setting_value = ? WHERE setting_key = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, value);
            ps.setString(2, key);
            ps.executeUpdate();
        }
    }

    private SystemSetting mapRow(ResultSet rs) throws SQLException {
        SystemSetting s = new SystemSetting();
        s.setId(rs.getInt("id"));
        s.setSettingKey(rs.getString("setting_key"));
        s.setSettingValue(rs.getString("setting_value"));
        s.setUpdatedAt(rs.getTimestamp("updated_at"));
        return s;
    }
}
