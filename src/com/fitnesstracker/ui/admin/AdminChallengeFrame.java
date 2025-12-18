package com.fitnesstracker.ui.admin;

import javax.swing.*;

public class AdminChallengeFrame extends JFrame {

    public AdminChallengeFrame() {
        setTitle("Admin - Challenges");
        setSize(400, 250);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea("Create, Update, Delete Fitness Challenges");
        add(new JScrollPane(area));

        setVisible(true);
    }
}
