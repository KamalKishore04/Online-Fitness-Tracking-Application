package com.fitnesstracker.service;

import com.fitnesstracker.model.Workout;

import java.time.LocalDate;
import java.util.List;

public interface WorkoutService {

    /**
     * Add a new workout for the user.
     */
    void addWorkout(Workout workout) throws Exception;

    /**
     * Update an existing workout.
     */
    void updateWorkout(Workout workout) throws Exception;

    /**
     * Delete a workout by id.
     */
    void deleteWorkout(int workoutId) throws Exception;

    /**
     * All workouts for a given user.
     */
    List<Workout> getWorkoutsForUser(int userId) throws Exception;

    /**
     * Workouts of a user between two dates (inclusive).
     */
    List<Workout> getWorkoutsForUserInRange(int userId,
                                            LocalDate from,
                                            LocalDate to) throws Exception;
}
