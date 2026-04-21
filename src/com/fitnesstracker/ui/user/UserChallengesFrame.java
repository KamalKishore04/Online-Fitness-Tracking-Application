package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.Challenge;
import com.fitnesstracker.model.User;
import com.fitnesstracker.model.UserChallenge;
import com.fitnesstracker.service.ChallengeService;
import com.fitnesstracker.service.UserChallengeService;
import com.fitnesstracker.service.impl.ChallengeServiceImpl;
import com.fitnesstracker.service.impl.UserChallengeServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserChallengesFrame extends JFrame {

    private final User loggedInUser;
    private final ChallengeService challengeService;
    private final UserChallengeService userChallengeService;

    private JTable challengeTable;
    private DefaultTableModel tableModel;

    public UserChallengesFrame(User user) {
        this.loggedInUser = user;
        this.challengeService = new ChallengeServiceImpl();
        this.userChallengeService = new UserChallengeServiceImpl();
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Available Challenges - " + loggedInUser.getName());
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("Active Fitness Challenges");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Start", "End", "Difficulty", "Joined?"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        challengeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(challengeTable);

        JButton joinButton = new JButton("Join Selected");
        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");

        joinButton.addActionListener(e -> handleJoin());
        refreshButton.addActionListener(e -> loadData());
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(joinButton);
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
            List<Challenge> active = challengeService.getActiveChallenges();
            List<UserChallenge> myChallenges = userChallengeService.getUserChallenges(loggedInUser.getId());

            Set<Integer> joinedIds = new HashSet<>();
            for (UserChallenge uc : myChallenges) {
                joinedIds.add(uc.getChallengeId());
            }

            for (Challenge c : active) {
                boolean joined = joinedIds.contains(c.getId());
                tableModel.addRow(new Object[]{
                        c.getId(),
                        c.getName(),
                        c.getStartDate(),
                        c.getEndDate(),
                        c.getDifficulty(),
                        joined ? "Yes" : "No"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading challenges: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer getSelectedChallengeId() {
        int row = challengeTable.getSelectedRow();
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

    private void handleJoin() {
        Integer challengeId = getSelectedChallengeId();
        if (challengeId == null) return;

        String joinedFlag = tableModel.getValueAt(challengeTable.getSelectedRow(), 5).toString();
        if ("Yes".equalsIgnoreCase(joinedFlag)) {
            JOptionPane.showMessageDialog(this,
                    "You already joined this challenge.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            userChallengeService.joinChallenge(loggedInUser.getId(), challengeId);
            JOptionPane.showMessageDialog(this,
                    "Joined challenge successfully!");
            loadData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
