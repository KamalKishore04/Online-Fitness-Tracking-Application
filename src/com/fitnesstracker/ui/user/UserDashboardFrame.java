package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.User;

import javax.swing.*;
import java.awt.*;

public class UserDashboardFrame extends JFrame {

    private final User user;

    public UserDashboardFrame(User user) {
        this.user = user;

        setTitle("User Dashboard - " + user.getName());
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton workoutBtn = new JButton("Workout Log");
        JButton challengeBtn = new JButton("Challenges");
        JButton historyBtn = new JButton("Challenge History");
        JButton profileBtn = new JButton("Profile");
        JButton submitBtn = new JButton("Submit Content");

        workoutBtn.addActionListener(e -> new WorkoutLogFrame(user));
        challengeBtn.addActionListener(e -> new ChallengeListFrame());
        historyBtn.addActionListener(e -> new ChallengeHistoryFrame());
        profileBtn.addActionListener(e -> new UserProfileFrame(user));
        submitBtn.addActionListener(e -> new FitnessContentSubmitFrame());

        setLayout(new GridLayout(5, 1, 10, 10));
        add(workoutBtn);
        add(challengeBtn);
        add(historyBtn);
        add(profileBtn);
        add(submitBtn);

        setVisible(true);
    }
}
