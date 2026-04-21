package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.FitnessContent;
import com.fitnesstracker.model.User;
import com.fitnesstracker.service.FitnessContentService;

import javax.swing.*;
import java.awt.*;

public class SubmitFitnessContentDialog extends JDialog {

    private final User loggedInUser;
    private final FitnessContentService contentService;

    private JTextField titleField;
    private JTextField typeField;
    private JTextField descriptionField;
    private JTextArea bodyArea;

    public SubmitFitnessContentDialog(Frame owner, User user, FitnessContentService service) {
        super(owner, "Submit Fitness Content", true);
        this.loggedInUser = user;
        this.contentService = service;
        initUI();
    }

    private void initUI() {
        setSize(600, 450);
        setLocationRelativeTo(getOwner());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Title:");
        JLabel typeLabel = new JLabel("Content Type (e.g., Tip, Article):");
        JLabel descLabel = new JLabel("Short Description:");
        JLabel bodyLabel = new JLabel("Content Body:");

        titleField = new JTextField(30);
        typeField = new JTextField(20);
        descriptionField = new JTextField(30);
        bodyArea = new JTextArea(8, 30);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        JScrollPane bodyScroll = new JScrollPane(bodyArea);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(titleLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(typeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(descLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(bodyLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(bodyScroll, gbc);

        JButton saveBtn = new JButton("Submit");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> handleSubmit());
        cancelBtn.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        getContentPane().setLayout(new BorderLayout(10,10));
        getContentPane().add(formPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleSubmit() {
        try {
            String title = titleField.getText().trim();
            String type = typeField.getText().trim();
            String desc = descriptionField.getText().trim();
            String body = bodyArea.getText().trim();

            FitnessContent c = new FitnessContent();
            c.setTitle(title);
            c.setContentType(type);
            c.setDescription(desc);
            c.setContentBody(body);

            contentService.submitContent(c, loggedInUser.getId());

            JOptionPane.showMessageDialog(this,
                    "Content submitted for review.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
