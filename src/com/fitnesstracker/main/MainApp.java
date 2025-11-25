package com.fitnesstracker.main;

import com.fitnesstracker.ui.auth.LoginFrame;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {

        // Optional: set a simple Look & Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
