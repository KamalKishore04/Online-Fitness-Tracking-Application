package com.fitnesstracker.service.impl;

import com.fitnesstracker.dao.ChallengeDao;
import com.fitnesstracker.dao.impl.ChallengeDaoImpl;
import com.fitnesstracker.model.Challenge;
import com.fitnesstracker.service.ChallengeService;

import java.time.LocalDate;
import java.util.List;

public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeDao challengeDao;

    public ChallengeServiceImpl() {
        this.challengeDao = new ChallengeDaoImpl();
    }

    @Override
    public void createChallenge(Challenge challenge, int createdBy) throws Exception {
        validate(challenge);
        challenge.setCreatedBy(createdBy);
        challengeDao.create(challenge);
    }

    @Override
    public void updateChallenge(Challenge challenge) throws Exception {
        if (challenge.getId() <= 0) {
            throw new IllegalArgumentException("Challenge id is required for update");
        }
        validate(challenge);
        challengeDao.update(challenge);
    }

    @Override
    public void deleteChallenge(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid challenge id");
        }
        challengeDao.delete(id);
    }

    @Override
    public List<Challenge> getAllChallenges() throws Exception {
        return challengeDao.findAll();
    }

    @Override
    public List<Challenge> getActiveChallenges() throws Exception {
        return challengeDao.findActive(LocalDate.now());
    }

    @Override
    public Challenge getById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid challenge id");
        return challengeDao.findById(id);
    }

    private void validate(Challenge c) {
        if (c.getName() == null || c.getName().isBlank()) {
            throw new IllegalArgumentException("Challenge name is required");
        }
        if (c.getStartDate() == null || c.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end dates are required");
        }
        if (c.getEndDate().isBefore(c.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        if (c.getDifficulty() == null || c.getDifficulty().isBlank()) {
            throw new IllegalArgumentException("Difficulty is required");
        }
    }
}
