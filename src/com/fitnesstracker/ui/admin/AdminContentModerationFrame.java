package com.fitnesstracker.ui.admin;

import javax.swing.*;

public class AdminContentModerationFrame extends JFrame {

    public AdminContentModerationFrame() {
        setTitle("Content Moderation");
        setSize(400, 250);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea("Approve or Reject User Submitted Content");
        add(new JScrollPane(area));

        setVisible(true);
    }
}
