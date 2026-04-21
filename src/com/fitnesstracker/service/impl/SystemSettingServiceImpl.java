package com.fitnesstracker.service.impl;

import com.fitnesstracker.dao.SystemSettingDao;
import com.fitnesstracker.dao.impl.SystemSettingDaoImpl;
import com.fitnesstracker.model.SystemSetting;
import com.fitnesstracker.service.SystemSettingService;

import java.util.List;

public class SystemSettingServiceImpl implements SystemSettingService {

    private final SystemSettingDao dao;

    public SystemSettingServiceImpl() {
        this.dao = new SystemSettingDaoImpl();
    }

    @Override
    public List<SystemSetting> getAllSettings() throws Exception {
        return dao.findAll();
    }

    @Override
    public void updateSetting(String key, String value) throws Exception {
        dao.updateValue(key, value);
    }

    @Override
    public String getSettingValue(String key, String defaultValue) throws Exception {
        SystemSetting s = dao.findByKey(key);
        if (s == null || s.getSettingValue() == null) {
            return defaultValue;
        }
        return s.getSettingValue();
    }
}
