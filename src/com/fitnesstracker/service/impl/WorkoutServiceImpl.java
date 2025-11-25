package com.fitnesstracker.service.impl;

import com.fitnesstracker.dao.WorkoutDao;
import com.fitnesstracker.dao.impl.WorkoutDaoImpl;
import com.fitnesstracker.logging.ActivityLogger;
import com.fitnesstracker.model.Workout;
import com.fitnesstracker.service.WorkoutService;

import java.time.LocalDate;
import java.util.List;

public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutDao workoutDao;

    public WorkoutServiceImpl() {
        this.workoutDao = new WorkoutDaoImpl();
    }

    @Override
    public void addWorkout(Workout workout) throws Exception {
        // ===== validation =====
        if (workout.getWorkoutDate() == null) {
            throw new IllegalArgumentException("Workout date is required");
        }
        if (workout.getWorkoutType() == null || workout.getWorkoutType().isBlank()) {
            throw new IllegalArgumentException("Workout type is required");
        }
        if (workout.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }

        // ===== DAO call – USE YOUR METHOD NAME =====
        workoutDao.createWorkout(workout);

        // ===== logging =====
        ActivityLogger.log(
                workout.getUserId(),
                "Added workout on " + workout.getWorkoutDate()
                        + ", type=" + workout.getWorkoutType()
                        + ", duration=" + workout.getDurationMinutes() + "min"
        );
    }

    @Override
    public void updateWorkout(Workout workout) throws Exception {
        if (workout.getId() <= 0) {
            throw new IllegalArgumentException("Invalid workout id");
        }

        // DAO call using your name
        workoutDao.updateWorkout(workout);

        ActivityLogger.log(
                workout.getUserId(),
                "Updated workout id=" + workout.getId()
        );
    }

    @Override
    public void deleteWorkout(int workoutId) throws Exception {
        // try to fetch first so we know which user to log against
        Workout existing = null;
        try {
            existing = workoutDao.findById(workoutId);
        } catch (Exception ignore) {
            // if it fails, still attempt delete; logging will fall back
        }

        // DAO call using your name
        workoutDao.deleteWorkout(workoutId);

        if (existing != null) {
            ActivityLogger.log(
                    existing.getUserId(),
                    "Deleted workout id=" + workoutId
            );
        } else {
            ActivityLogger.log(
                    null,
                    "Deleted workout id=" + workoutId + " (user unknown)"
            );
        }
    }

    @Override
    public List<Workout> getWorkoutsForUser(int userId) throws Exception {
        return workoutDao.findByUser(userId);
    }

    @Override
    public List<Workout> getWorkoutsForUserInRange(int userId,
                                                   LocalDate from,
                                                   LocalDate to) throws Exception {
        return workoutDao.findByUserAndDateRange(userId, from, to);
    }
}
