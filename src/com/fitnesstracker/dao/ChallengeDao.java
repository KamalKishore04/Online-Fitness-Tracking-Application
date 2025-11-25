package com.fitnesstracker.dao;

import com.fitnesstracker.model.Challenge;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ChallengeDao {

    void create(Challenge challenge) throws SQLException;

    void update(Challenge challenge) throws SQLException;

    void delete(int id) throws SQLException;

    Challenge findById(int id) throws SQLException;

    List<Challenge> findAll() throws SQLException;

    List<Challenge> findActive(LocalDate today) throws SQLException;
}
