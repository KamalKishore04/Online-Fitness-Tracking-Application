package com.fitnesstracker.ui.auth;

import com.fitnesstracker.logging.ActivityLogger;
import com.fitnesstracker.model.User;
import com.fitnesstracker.service.AuthService;
import com.fitnesstracker.service.impl.AuthServiceImpl;
import com.fitnesstracker.ui.admin.AdminDashboardFrame;
import com.fitnesstracker.ui.user.UserDashboardFrame;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    private final AuthService authService;

    public LoginFrame() {
        this.authService = new AuthServiceImpl();
        initUI();
    }

    private void initUI() {
        setTitle("Online Fitness Tracker - Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");

        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JButton loginBtn = new JButton("Login");
        JButton exitBtn = new JButton("Exit");

        loginBtn.addActionListener(e -> handleLogin());
        exitBtn.addActionListener(e -> System.exit(0));

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(loginBtn, gbc);
        gbc.gridx = 1;
        panel.add(exitBtn, gbc);

        setContentPane(panel);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Email and password are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 🔥 use your existing AuthService method here
            User user = authService.login(email, password);

            if (user == null) {
                ActivityLogger.log(null, "Failed login attempt for email=" + email);
                JOptionPane.showMessageDialog(this,
                        "Invalid credentials.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Successful login – log it
            ActivityLogger.log(user.getId(), "Logged in as " + user.getRole());

            JOptionPane.showMessageDialog(this,
                    "Welcome, " + user.getName(),
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                SwingUtilities.invokeLater(() ->
                        new AdminDashboardFrame(user).setVisible(true));
            } else {
                SwingUtilities.invokeLater(() ->
                        new UserDashboardFrame(user).setVisible(true));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error during login: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // main for direct run
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
