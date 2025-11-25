package com.fitnesstracker.service;

import com.fitnesstracker.model.User;

public interface AuthService {

    User login(String email, String password) throws Exception;

    User register(String name, String email, String password, String role) throws Exception;
}
