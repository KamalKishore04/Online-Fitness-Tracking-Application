package com.fitnesstracker.service.impl;

import com.fitnesstracker.dao.UserDao;
import com.fitnesstracker.dao.impl.UserDaoImpl;
import com.fitnesstracker.model.User;
import com.fitnesstracker.service.AuthService;

public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;

    public AuthServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    @Override
    public User login(String email, String password) throws Exception {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Email and password are required");
        }

        User user = userDao.findByEmailAndPassword(email.trim(), password.trim());
        if (user == null) {
            throw new Exception("Invalid email or password");
        }

        return user;
    }

    @Override
    public User register(String name, String email, String password, String role) throws Exception {
        if (name == null || name.isBlank() ||
                email == null || email.isBlank() ||
                password == null || password.isBlank()) {
            throw new IllegalArgumentException("Name, email, and password are required");
        }

        if (role == null || role.isBlank()) {
            role = "USER";
        }

        if (userDao.emailExists(email.trim())) {
            throw new Exception("Email already registered");
        }

        User user = new User(name.trim(), email.trim(), password.trim(), role.trim().toUpperCase());
        userDao.createUser(user);
        return user;
    }
}
