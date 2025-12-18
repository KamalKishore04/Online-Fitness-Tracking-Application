package com.fitnesstracker.ui.user;

import javax.swing.*;

public class ChallengeHistoryFrame extends JFrame {

    public ChallengeHistoryFrame() {
        setTitle("Challenge History");
        setSize(350, 200);
        setLocationRelativeTo(null);

        add(new JLabel("Completed Challenges", SwingConstants.CENTER));
        setVisible(true);
    }
}
