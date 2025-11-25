package com.fitnesstracker.dao;

import com.fitnesstracker.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    User findByEmailAndPassword(String email, String password) throws SQLException;

    User findByEmail(String email) throws SQLException;

    User findById(int id) throws SQLException;

    boolean emailExists(String email) throws SQLException;

    void createUser(User user) throws SQLException;

    void updateUser(User user) throws SQLException;

    void deleteUser(int id) throws SQLException;

    List<User> findAllUsers() throws SQLException;
}
