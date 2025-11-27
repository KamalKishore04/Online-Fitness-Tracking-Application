package com.fitnesstracker.service;

import com.fitnesstracker.model.UserChallenge;

import java.util.List;

public interface UserChallengeService {

    /**
     * User joins a challenge. Should prevent duplicate join.
     */
    void joinChallenge(int userId, int challengeId) throws Exception;

    /**
     * Returns all challenge records for a given user.
     */
    List<UserChallenge> getUserChallenges(int userId) throws Exception;
}
