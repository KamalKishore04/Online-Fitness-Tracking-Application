package com.fitnesstracker.service.impl;

import com.fitnesstracker.dao.UserChallengeDao;
import com.fitnesstracker.dao.impl.UserChallengeDaoImpl;
import com.fitnesstracker.logging.ActivityLogger;
import com.fitnesstracker.model.UserChallenge;
import com.fitnesstracker.service.UserChallengeService;

import java.util.List;

public class UserChallengeServiceImpl implements UserChallengeService {

    private final UserChallengeDao dao;

    public UserChallengeServiceImpl() {
        this.dao = new UserChallengeDaoImpl();
    }

    @Override
    public void joinChallenge(int userId, int challengeId) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }
        if (challengeId <= 0) {
            throw new IllegalArgumentException("Invalid challenge id");
        }

        // ✅ use the existing DAO method that checks duplicates
        if (dao.exists(userId, challengeId)) {
            throw new IllegalArgumentException("You already joined this challenge");
        }

        // ✅ create a UserChallenge object and use dao.create(UserChallenge)
        UserChallenge uc = new UserChallenge();
        uc.setUserId(userId);
        uc.setChallengeId(challengeId);
        uc.setStatus("IN_PROGRESS");   // or whatever default you used earlier

        dao.create(uc);

        // ✅ log the action
        ActivityLogger.log(userId, "Joined challenge id=" + challengeId);
    }

    @Override
    public List<UserChallenge> getUserChallenges(int userId) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }
        // ✅ use the existing DAO method that returns all for a user
        return dao.findByUser(userId);
    }
}
