package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.User;

import javax.swing.*;
import java.awt.*;

public class UserDashboardFrame extends JFrame {

    private final User loggedInUser;

    public UserDashboardFrame(User user) {
        this.loggedInUser = user;
        initUI();
    }

    private void initUI() {
        setTitle("User Dashboard - Online Fitness Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("User Dashboard - Welcome, " + loggedInUser.getName());
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel centerPanel = new JPanel(new GridLayout(3, 3, 15, 15));

        JButton workoutLogBtn        = new JButton("Workout Log");
        JButton progressTrackingBtn  = new JButton("Progress Tracking");
        JButton fitnessChallengesBtn = new JButton("Fitness Challenges");
        JButton fitnessContentBtn    = new JButton("Fitness Content");
        JButton challengeHistoryBtn  = new JButton("Challenge History");
        JButton profileBtn           = new JButton("Profile");
        JButton logoutBtn            = new JButton("Logout");

        centerPanel.add(workoutLogBtn);
        centerPanel.add(progressTrackingBtn);
        centerPanel.add(fitnessChallengesBtn);
        centerPanel.add(fitnessContentBtn);
        centerPanel.add(challengeHistoryBtn);
        centerPanel.add(profileBtn);
        centerPanel.add(new JLabel()); // empty cells
        centerPanel.add(logoutBtn);
        centerPanel.add(new JLabel());

        workoutLogBtn.addActionListener(e ->
                SwingUtilities.invokeLater(() ->
                        new WorkoutLogFrame(loggedInUser).setVisible(true)));

        progressTrackingBtn.addActionListener(e ->
                SwingUtilities.invokeLater(() ->
                        new ProgressTrackingFrame(loggedInUser).setVisible(true)));

        fitnessChallengesBtn.addActionListener(e ->
                SwingUtilities.invokeLater(() ->
                        new UserChallengesFrame(loggedInUser).setVisible(true)));

        fitnessContentBtn.addActionListener(e ->
                SwingUtilities.invokeLater(() ->
                        new FitnessContentListFrame(loggedInUser).setVisible(true)));

        challengeHistoryBtn.addActionListener(e ->
                SwingUtilities.invokeLater(() ->
                        new ChallengeHistoryFrame(loggedInUser).setVisible(true)));

        profileBtn.addActionListener(e ->
                SwingUtilities.invokeLater(() ->
                        new UserProfileFrame(loggedInUser).setVisible(true)));

        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                JOptionPane.showMessageDialog(null, "Logged out successfully.");
            }
        });

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(welcomeLabel, BorderLayout.NORTH);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
    }
}
