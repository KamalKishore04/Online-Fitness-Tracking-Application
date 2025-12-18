package com.fitnesstracker.ui.admin;

import javax.swing.*;

public class AdminSystemSettingsFrame extends JFrame {

    public AdminSystemSettingsFrame() {
        setTitle("System Settings");
        setSize(350, 200);
        setLocationRelativeTo(null);

        add(new JLabel("Application Configuration Settings", SwingConstants.CENTER));

        setVisible(true);
    }
}
