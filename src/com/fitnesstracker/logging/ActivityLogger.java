package com.fitnesstracker.logging;

import com.fitnesstracker.service.ActivityLogService;
import com.fitnesstracker.service.impl.ActivityLogServiceImpl;

/**
 * Simple static helper to log actions from anywhere.
 */
public class ActivityLogger {

    private static final ActivityLogService service = new ActivityLogServiceImpl();

    public static void log(Integer userId, String action) {
        try {
            service.log(userId, action);
        } catch (Exception e) {
            // Don't crash app because of logging failure
            e.printStackTrace();
        }
    }
}
