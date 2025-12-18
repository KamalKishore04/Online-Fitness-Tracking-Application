package com.fitnesstracker.ui.user;

import javax.swing.*;

public class ChallengeListFrame extends JFrame {

    public ChallengeListFrame() {
        setTitle("Available Challenges");
        setSize(350, 200);
        setLocationRelativeTo(null);

        add(new JLabel("List of Fitness Challenges", SwingConstants.CENTER));
        setVisible(true);
    }
}
