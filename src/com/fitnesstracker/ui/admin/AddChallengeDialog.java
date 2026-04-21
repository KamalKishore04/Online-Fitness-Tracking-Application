package com.fitnesstracker.ui.admin;

import com.fitnesstracker.model.Challenge;
import com.fitnesstracker.model.User;
import com.fitnesstracker.service.ChallengeService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddChallengeDialog extends JDialog {

    private final ChallengeService challengeService;
    private final User adminUser;

    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField startDateField;
    private JTextField endDateField;
    private JComboBox<String> difficultyCombo;
    private JTextField targetWorkoutsField;
    private JTextField targetMinutesField;

    public AddChallengeDialog(Frame owner, ChallengeService challengeService, User adminUser) {
        super(owner, "Add Challenge", true);
        this.challengeService = challengeService;
        this.adminUser = adminUser;
        initUI();
    }

    private void initUI() {
        setSize(550, 450);
        setLocationRelativeTo(getOwner());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Name:");
        JLabel descLabel = new JLabel("Description:");
        JLabel startLabel = new JLabel("Start Date (yyyy-MM-dd):");
        JLabel endLabel = new JLabel("End Date (yyyy-MM-dd):");
        JLabel diffLabel = new JLabel("Difficulty:");
        JLabel targetWLabel = new JLabel("Target Workouts (optional):");
        JLabel targetMLabel = new JLabel("Target Minutes (optional):");

        nameField = new JTextField(25);
        descriptionArea = new JTextArea(5, 25);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);

        startDateField = new JTextField(15);
        endDateField = new JTextField(15);
        difficultyCombo = new JComboBox<>(new String[]{"EASY", "MEDIUM", "HARD"});

        targetWorkoutsField = new JTextField(15);
        targetMinutesField = new JTextField(15);

        // Row 0: Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Row 1: Description
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(descLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(descScroll, gbc);

        // Row 2: Start date
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(startLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(startDateField, gbc);

        // Row 3: End date
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(endLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(endDateField, gbc);

        // Row 4: Difficulty
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(diffLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(difficultyCombo, gbc);

        // Row 5: Target workouts
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(targetWLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(targetWorkoutsField, gbc);

        // Row 6: Target minutes
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(targetMLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(targetMinutesField, gbc);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        getContentPane().setLayout(new BorderLayout(10,10));
        getContentPane().add(formPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleSave() {
        try {
            String name = nameField.getText().trim();
            String desc = descriptionArea.getText().trim();
            LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
            LocalDate endDate = LocalDate.parse(endDateField.getText().trim());
            String diff = (String) difficultyCombo.getSelectedItem();

            String twText = targetWorkoutsField.getText().trim();
            String tmText = targetMinutesField.getText().trim();
            Integer targetWorkouts = twText.isEmpty() ? null : Integer.parseInt(twText);
            Integer targetMinutes = tmText.isEmpty() ? null : Integer.parseInt(tmText);

            Challenge c = new Challenge();
            c.setName(name);
            c.setDescription(desc);
            c.setStartDate(startDate);
            c.setEndDate(endDate);
            c.setDifficulty(diff);
            c.setTargetWorkouts(targetWorkouts);
            c.setTargetMinutes(targetMinutes);

            challengeService.createChallenge(c, adminUser.getId());
            JOptionPane.showMessageDialog(this, "Challenge created.");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Target workouts/minutes must be numbers.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
