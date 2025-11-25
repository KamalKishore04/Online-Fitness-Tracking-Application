package com.fitnesstracker.dao;

import com.fitnesstracker.model.UserChallenge;

import java.sql.SQLException;
import java.util.List;

public interface UserChallengeDao {

    void create(UserChallenge uc) throws SQLException;

    boolean exists(int userId, int challengeId) throws SQLException;

    List<UserChallenge> findByUser(int userId) throws SQLException;

    List<UserChallenge> findByUserAndStatus(int userId, String status) throws SQLException;
}

