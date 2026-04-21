package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.User;
import com.fitnesstracker.service.ProfileService;
import com.fitnesstracker.service.impl.ProfileServiceImpl;
import com.fitnesstracker.logging.ActivityLogger;

import javax.swing.*;
import java.awt.*;

public class UserProfileFrame extends JFrame {

    private final User loggedInUser;
    private final ProfileService profileService;

    private JTextField nameField;
    private JTextField emailField;

    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public UserProfileFrame(User user) {
        this.loggedInUser = user;
        this.profileService = new ProfileServiceImpl();
        initUI();
        loadUserData();
    }

    private void initUI() {
        setTitle("Profile - " + loggedInUser.getName());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Profile Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // ==== PROFILE INFO PANEL ====
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBorder(BorderFactory.createTitledBorder("Profile Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");

        nameField = new JTextField(25);
        emailField = new JTextField(25);

        gbc.gridx = 0; gbc.gridy = 0;
        profilePanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        profilePanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        profilePanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        profilePanel.add(emailField, gbc);

        JButton saveProfileBtn = new JButton("Save Profile");
        saveProfileBtn.addActionListener(e -> saveProfile());

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        profilePanel.add(saveProfileBtn, gbc);

        // ==== PASSWORD PANEL ====
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Change Password"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5,5,5,5);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        JLabel oldPassLabel = new JLabel("Old Password:");
        JLabel newPassLabel = new JLabel("New Password:");
        JLabel confirmPassLabel = new JLabel("Confirm New Password:");

        oldPasswordField = new JPasswordField(20);
        newPasswordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);

        gbc2.gridx = 0; gbc2.gridy = 0;
        passwordPanel.add(oldPassLabel, gbc2);
        gbc2.gridx = 1;
        passwordPanel.add(oldPasswordField, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 1;
        passwordPanel.add(newPassLabel, gbc2);
        gbc2.gridx = 1;
        passwordPanel.add(newPasswordField, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 2;
        passwordPanel.add(confirmPassLabel, gbc2);
        gbc2.gridx = 1;
        passwordPanel.add(confirmPasswordField, gbc2);

        JButton changePassBtn = new JButton("Change Password");
        changePassBtn.addActionListener(e -> changePassword());

        gbc2.gridx = 1; gbc2.gridy = 3;
        gbc2.anchor = GridBagConstraints.EAST;
        passwordPanel.add(changePassBtn, gbc2);

        centerPanel.add(profilePanel);
        centerPanel.add(passwordPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(closeBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void loadUserData() {
        try {
            User fresh = profileService.getUserById(loggedInUser.getId());
            if (fresh != null) {
                nameField.setText(fresh.getName());
                emailField.setText(fresh.getEmail());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading profile: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveProfile() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        try {
            profileService.updateProfile(loggedInUser.getId(), name, email);
            loggedInUser.setName(name);
            loggedInUser.setEmail(email);
            ActivityLogger.log(loggedInUser.getId(), "Updated profile information");
            JOptionPane.showMessageDialog(this, "Profile updated successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changePassword() {
        String oldPass = new String(oldPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if (!newPass.equals(confirm)) {
            JOptionPane.showMessageDialog(this,
                    "New password and confirm password do not match.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            profileService.changePassword(loggedInUser.getId(), oldPass, newPass);
            ActivityLogger.log(loggedInUser.getId(), "Changed password");
            JOptionPane.showMessageDialog(this, "Password changed successfully.");
            oldPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
