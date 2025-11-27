package com.fitnesstracker.service;

import com.fitnesstracker.model.User;

public interface ProfileService {

    User getUserById(int userId) throws Exception;

    void updateProfile(int userId, String name, String email) throws Exception;

    void changePassword(int userId, String oldPassword, String newPassword) throws Exception;
}
