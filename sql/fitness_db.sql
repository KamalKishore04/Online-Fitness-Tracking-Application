-- =========================================
-- CLEAN DATABASE FOR ONLINE FITNESS TRACKER
-- =========================================

-- Drop old database if you want a full reset
DROP DATABASE IF EXISTS fitness_db;

CREATE DATABASE fitness_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE fitness_db;

-- =====================
-- 1. USERS
-- =====================
-- Admin + normal users share this table

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,           -- matches Java code (NOT password_hash)
    role ENUM('ADMIN','USER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample admin + normal user
INSERT INTO users (name, email, password, role) VALUES
    ('Admin User', 'admin@fit.com', 'admin123', 'ADMIN'),
    ('Test User',  'user1@fit.com', 'user123',  'USER');

-- =====================
-- 2. WORKOUTS
-- =====================
-- Core tracking feature (matches current WorkoutDaoImpl using column `type`)

CREATE TABLE workouts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    workout_date DATE NOT NULL,
    type VARCHAR(50) NOT NULL,                -- IMPORTANT: column is `type`, not `workout_type`
    duration_minutes INT NOT NULL,
    intensity ENUM('LOW','MEDIUM','HIGH') NOT NULL,
    notes VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =====================
-- 3. CHALLENGES
-- =====================
-- Admin-defined fitness challenges
-- User joins via user_challenges

CREATE TABLE challenges (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    difficulty ENUM('EASY','MEDIUM','HARD') NOT NULL,  -- REQUIRED BY DAO
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- USED IN ORDER BY
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- =====================
-- 4. USER_CHALLENGES
-- =====================
-- Mapping of user ↔ challenge with status

CREATE TABLE user_challenges (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    challenge_id INT NOT NULL,
    status ENUM('JOINED','COMPLETED') NOT NULL DEFAULT 'JOINED',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (challenge_id) REFERENCES challenges(id)
);

-- =====================
-- 5. FITNESS CONTENT
-- =====================
-- For "Fitness Content" admin feature (future / optional)

CREATE TABLE fitness_content (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    content_type VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    content_body TEXT,
    status ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
    submitted_by INT,
    reviewed_by INT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP NULL,
    FOREIGN KEY (submitted_by) REFERENCES users(id),
    FOREIGN KEY (reviewed_by) REFERENCES users(id)
);

-- =====================
-- 6. SYSTEM SETTINGS
-- =====================

CREATE TABLE system_settings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Default settings
INSERT INTO system_settings (setting_key, setting_value) VALUES
    ('DAILY_STEP_GOAL', '10000'),
    ('DEFAULT_WORKOUT_INTENSITY', 'MEDIUM')
ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value);

-- =====================
-- 7. ACTIVITY LOG
-- =====================

CREATE TABLE activity_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action VARCHAR(255) NOT NULL,
    action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =====================
-- 8. SIMPLE CHECKS (optional)
-- =====================

-- SHOW TABLES;
-- DESCRIBE users;
-- DESCRIBE workouts;
-- DESCRIBE challenges;
-- DESCRIBE user_challenges;
-- DESCRIBE fitness_content;
-- DESCRIBE system_settings;
-- DESCRIBE activity_log;

USE fitness_db;

ALTER TABLE challenges
    ADD COLUMN target_workouts INT NULL,
    ADD COLUMN target_minutes INT NULL;

DESCRIBE challenges;
