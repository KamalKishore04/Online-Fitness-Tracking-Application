package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.Workout;
import com.fitnesstracker.service.WorkoutService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class EditWorkoutDialog extends JDialog {

    private final WorkoutService workoutService;
    private final Workout workout;

    private JTextField dateField;
    private JTextField typeField;
    private JTextField durationField;
    private JComboBox<String> intensityCombo;
    private JTextField notesField;

    public EditWorkoutDialog(Frame owner, WorkoutService workoutService, Workout workout) {
        super(owner, "Edit Workout", true);
        this.workoutService = workoutService;
        this.workout = workout;
        initUI();
        fillForm();
    }

    private void initUI() {
        setSize(450, 300);
        setLocationRelativeTo(getOwner());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
        JLabel typeLabel = new JLabel("Workout Type:");
        JLabel durationLabel = new JLabel("Duration (minutes):");
        JLabel intensityLabel = new JLabel("Intensity:");
        JLabel notesLabel = new JLabel("Notes:");

        dateField = new JTextField(20);
        typeField = new JTextField(20);
        durationField = new JTextField(20);
        intensityCombo = new JComboBox<>(new String[]{"LOW", "MEDIUM", "HIGH"});
        notesField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(dateLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(typeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(durationLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(durationField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(intensityLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(intensityCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(notesLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(notesField, gbc);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(formPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void fillForm() {
        dateField.setText(workout.getWorkoutDate().toString());
        typeField.setText(workout.getWorkoutType());
        durationField.setText(String.valueOf(workout.getDurationMinutes()));
        intensityCombo.setSelectedItem(workout.getIntensity());
        notesField.setText(workout.getNotes() == null ? "" : workout.getNotes());
    }

    private void handleSave() {
        try {
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            String type = typeField.getText().trim();
            int duration = Integer.parseInt(durationField.getText().trim());
            String intensity = (String) intensityCombo.getSelectedItem();
            String notes = notesField.getText().trim();
            if (notes.isBlank()) notes = null;

            workout.setWorkoutDate(date);
            workout.setWorkoutType(type);
            workout.setDurationMinutes(duration);
            workout.setIntensity(intensity);
            workout.setNotes(notes);

            workoutService.updateWorkout(workout);
            JOptionPane.showMessageDialog(this, "Workout updated.");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Duration must be a number.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
