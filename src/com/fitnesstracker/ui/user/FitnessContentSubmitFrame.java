package com.fitnesstracker.ui.user;

import javax.swing.*;

public class FitnessContentSubmitFrame extends JFrame {

    public FitnessContentSubmitFrame() {
        setTitle("Submit Fitness Content");
        setSize(400, 250);
        setLocationRelativeTo(null);

        JTextArea content = new JTextArea();
        JButton submit = new JButton("Submit");

        setLayout(new BorderLayout());
        add(new JScrollPane(content), BorderLayout.CENTER);
        add(submit, BorderLayout.SOUTH);

        setVisible(true);
    }
}
