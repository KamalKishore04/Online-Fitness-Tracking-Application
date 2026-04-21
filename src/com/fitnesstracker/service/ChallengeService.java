package com.fitnesstracker.service;

import com.fitnesstracker.model.Challenge;

import java.util.List;

public interface ChallengeService {

    void createChallenge(Challenge challenge, int createdBy) throws Exception;

    void updateChallenge(Challenge challenge) throws Exception;

    void deleteChallenge(int id) throws Exception;

    List<Challenge> getAllChallenges() throws Exception;

    List<Challenge> getActiveChallenges() throws Exception;

    Challenge getById(int id) throws Exception;
}
