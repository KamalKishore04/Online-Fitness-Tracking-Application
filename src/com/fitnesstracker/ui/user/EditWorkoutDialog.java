package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.Workout;
import com.fitnesstracker.service.WorkoutService;

import javax.swing.*;

public class EditWorkoutDialog extends JDialog {

    public EditWorkoutDialog(JFrame parent, WorkoutService service, Workout workout) {
        super(parent, "Edit Workout", true);
        setSize(300, 250);
        setLocationRelativeTo(parent);

        JTextField type = new JTextField(workout.getWorkoutType());
        JTextField duration = new JTextField(String.valueOf(workout.getDurationMinutes()));
        JTextField intensity = new JTextField(workout.getIntensity());

        JButton update = new JButton("Update");

        update.addActionListener(e -> {
            workout.setWorkoutType(type.getText());
            workout.setDurationMinutes(Integer.parseInt(duration.getText()));
            workout.setIntensity(intensity.getText());

            service.updateWorkout(workout);
            dispose();
        });

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Type"));
        add(type);
        add(new JLabel("Duration"));
        add(duration);
        add(new JLabel("Intensity"));
        add(intensity);
        add(update);

        setVisible(true);
    }
}
