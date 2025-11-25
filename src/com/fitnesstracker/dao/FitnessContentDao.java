package com.fitnesstracker.dao;

import com.fitnesstracker.model.FitnessContent;

import java.sql.SQLException;
import java.util.List;

public interface FitnessContentDao {

    void create(FitnessContent content) throws SQLException;

    void updateStatus(int id, String status, Integer reviewedBy) throws SQLException;

    FitnessContent findById(int id) throws SQLException;

    List<FitnessContent> findAll() throws SQLException;

    List<FitnessContent> findByStatus(String status) throws SQLException;

    List<FitnessContent> findApproved() throws SQLException;

    List<FitnessContent> findByUser(int userId) throws SQLException;
}
