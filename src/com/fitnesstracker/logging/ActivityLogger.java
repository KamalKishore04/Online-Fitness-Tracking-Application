package com.fitnesstracker.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityLogger {

    private static final String LOG_FILE = "activity.log";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ActivityLogger() {
    }

    public static void log(int userId, String message) {
        writeLog("USER_ID=" + userId + " | " + message);
    }

    public static void logSystem(String message) {
        writeLog("SYSTEM | " + message);
    }

    private static synchronized void writeLog(String content) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            String time = LocalDateTime.now().format(FORMATTER);
            writer.write(time + " - " + content + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }
    }
}

