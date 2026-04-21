package com.fitnesstracker.service;

import com.fitnesstracker.model.SystemSetting;

import java.util.List;

public interface SystemSettingService {

    List<SystemSetting> getAllSettings() throws Exception;

    void updateSetting(String key, String value) throws Exception;

    String getSettingValue(String key, String defaultValue) throws Exception;
}
