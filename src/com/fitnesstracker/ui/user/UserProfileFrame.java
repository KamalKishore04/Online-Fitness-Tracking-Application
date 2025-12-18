package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.User;

import javax.swing.*;

public class UserProfileFrame extends JFrame {

    public UserProfileFrame(User user) {
        setTitle("User Profile");
        setSize(300, 200);
        setLocationRelativeTo(null);

        add(new JLabel("Name: " + user.getName(), SwingConstants.CENTER));
        setVisible(true);
    }
}
