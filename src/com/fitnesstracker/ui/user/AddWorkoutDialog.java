package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.User;
import com.fitnesstracker.model.Workout;
import com.fitnesstracker.service.WorkoutService;

import javax.swing.*;
import java.time.LocalDate;

public class AddWorkoutDialog extends JDialog {

    public AddWorkoutDialog(JFrame parent, User user, WorkoutService service) {
        super(parent, "Add Workout", true);
        setSize(300, 250);
        setLocationRelativeTo(parent);

        JTextField type = new JTextField();
        JTextField duration = new JTextField();
        JTextField intensity = new JTextField();

        JButton save = new JButton("Save");

        save.addActionListener(e -> {
            Workout w = new Workout();
            w.setUserId(user.getId());
            w.setWorkoutDate(LocalDate.now());
            w.setWorkoutType(type.getText());
            w.setDurationMinutes(Integer.parseInt(duration.getText()));
            w.setIntensity(intensity.getText());

            service.addWorkout(w);
            dispose();
        });

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Type"));
        add(type);
        add(new JLabel("Duration (min)"));
        add(duration);
        add(new JLabel("Intensity"));
        add(intensity);
        add(save);

        setVisible(true);
    }
}
