package com.fitnesstracker.dao;

import com.fitnesstracker.model.Workout;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface WorkoutDao {

    void createWorkout(Workout workout) throws SQLException;

    void updateWorkout(Workout workout) throws SQLException;

    void deleteWorkout(int id) throws SQLException;

    Workout findById(int id) throws SQLException;

    List<Workout> findByUser(int userId) throws SQLException;

    List<Workout> findByUserAndDateRange(int userId, LocalDate from, LocalDate to) throws SQLException;
}
