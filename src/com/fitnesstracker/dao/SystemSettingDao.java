package com.fitnesstracker.dao;

import com.fitnesstracker.model.SystemSetting;

import java.sql.SQLException;
import java.util.List;

public interface SystemSettingDao {

    List<SystemSetting> findAll() throws SQLException;

    SystemSetting findByKey(String key) throws SQLException;

    void updateValue(String key, String value) throws SQLException;
}
