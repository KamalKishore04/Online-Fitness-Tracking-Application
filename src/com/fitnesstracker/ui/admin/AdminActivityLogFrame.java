package com.fitnesstracker.ui.admin;

import javax.swing.*;

public class AdminActivityLogFrame extends JFrame {

    public AdminActivityLogFrame() {
        setTitle("Activity Logs");
        setSize(400, 250);
        setLocationRelativeTo(null);

        JTextArea logs = new JTextArea("Admin & User Activity Logs");
        add(new JScrollPane(logs));

        setVisible(true);
    }
}
