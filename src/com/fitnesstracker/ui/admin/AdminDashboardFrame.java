package com.fitnesstracker.ui.admin;

import com.fitnesstracker.model.User;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardFrame extends JFrame {

    private final User loggedInUser;

    public AdminDashboardFrame(User user) {
        this.loggedInUser = user;
        initUI();
    }

    private void initUI() {
        setTitle("Admin Dashboard - Online Fitness Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Admin Dashboard - Welcome, " + loggedInUser.getName());
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel centerPanel = new JPanel(new GridLayout(2, 3, 15, 15));

        JButton userManagementBtn       = new JButton("User Management");
        JButton challengesManagementBtn = new JButton("Challenges Management");
        JButton fitnessContentBtn       = new JButton("Fitness Content");
        JButton systemSettingsBtn       = new JButton("System Settings");
        JButton systemActivityBtn       = new JButton("System Activity");
        JButton logoutBtn               = new JButton("Logout");

        centerPanel.add(userManagementBtn);
        centerPanel.add(challengesManagementBtn);
        centerPanel.add(fitnessContentBtn);
        centerPanel.add(systemSettingsBtn);
        centerPanel.add(systemActivityBtn);
        centerPanel.add(logoutBtn);

        userManagementBtn.addActionListener(e ->
                SwingUtilities.invokeLater(() ->
                        new UserManagementFrame(loggedInUser).setVisible(true)));

        challengesManagementBtn.addActionListener(e ->
                SwingUtilities.invokeLater(() ->
                        new AdminChallengesFrame(loggedInUser).setVisible(true)));

        fitnessContentBtn.addActionListener(e ->
                SwingUtilities.invokeLater(() ->
                        new AdminFitnessContentFrame(loggedInUser).setVisible(true)));

        systemSettingsBtn.addActionListener(e ->
                SwingUtilities.invokeLater(() ->
                        new AdminSystemSettingsFrame(loggedInUser).setVisible(true)));

        systemActivityBtn.addActionListener(e ->
                SwingUtilities.invokeLater(() ->
                        new AdminActivityLogFrame(loggedInUser).setVisible(true)));

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
