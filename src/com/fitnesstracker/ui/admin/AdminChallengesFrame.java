package com.fitnesstracker.ui.admin;

import com.fitnesstracker.model.Challenge;
import com.fitnesstracker.model.User;
import com.fitnesstracker.service.ChallengeService;
import com.fitnesstracker.service.impl.ChallengeServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminChallengesFrame extends JFrame {

    private final User adminUser;
    private final ChallengeService challengeService;

    private JTable table;
    private DefaultTableModel tableModel;

    public AdminChallengesFrame(User adminUser) {
        this.adminUser = adminUser;
        this.challengeService = new ChallengeServiceImpl();
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Challenges Management - Admin");
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("Manage Fitness Challenges");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Start", "End", "Difficulty", "Target W", "Target Min"},
                0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");

        addButton.addActionListener(e -> openAddDialog());
        editButton.addActionListener(e -> openEditDialog());
        deleteButton.addActionListener(e -> deleteSelected());
        refreshButton.addActionListener(e -> loadData());
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(titleLabel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<Challenge> list = challengeService.getAllChallenges();
            for (Challenge c : list) {
                tableModel.addRow(new Object[]{
                        c.getId(),
                        c.getName(),
                        c.getStartDate(),
                        c.getEndDate(),
                        c.getDifficulty(),
                        c.getTargetWorkouts(),
                        c.getTargetMinutes()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading challenges: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer getSelectedId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a challenge.",
                    "No selection",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        Object val = tableModel.getValueAt(row, 0);
        return Integer.parseInt(val.toString());
    }

    private void openAddDialog() {
        AddChallengeDialog dialog = new AddChallengeDialog(this, challengeService, adminUser);
        dialog.setVisible(true);
        loadData();
    }

    private void openEditDialog() {
        Integer id = getSelectedId();
        if (id == null) return;

        try {
            Challenge c = challengeService.getById(id);
            if (c == null) {
                JOptionPane.showMessageDialog(this,
                        "Challenge not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            EditChallengeDialog dialog = new EditChallengeDialog(this, challengeService, c);
            dialog.setVisible(true);
            loadData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelected() {
        Integer id = getSelectedId();
        if (id == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected challenge?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            challengeService.deleteChallenge(id);
            JOptionPane.showMessageDialog(this,
                    "Challenge deleted.");
            loadData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
