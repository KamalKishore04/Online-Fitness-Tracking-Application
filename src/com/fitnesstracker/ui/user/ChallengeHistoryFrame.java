package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.Challenge;
import com.fitnesstracker.model.User;
import com.fitnesstracker.model.UserChallenge;
import com.fitnesstracker.model.Workout;
import com.fitnesstracker.service.ChallengeService;
import com.fitnesstracker.service.UserChallengeService;
import com.fitnesstracker.service.WorkoutService;
import com.fitnesstracker.service.impl.ChallengeServiceImpl;
import com.fitnesstracker.service.impl.UserChallengeServiceImpl;
import com.fitnesstracker.service.impl.WorkoutServiceImpl;
import com.fitnesstracker.logging.ActivityLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;

public class ChallengeHistoryFrame extends JFrame {

    private final User loggedInUser;
    private final UserChallengeService userChallengeService;
    private final ChallengeService challengeService;
    private final WorkoutService workoutService;

    private JTable table;
    private DefaultTableModel tableModel;

    public ChallengeHistoryFrame(User user) {
        this.loggedInUser = user;
        this.userChallengeService = new UserChallengeServiceImpl();
        this.challengeService = new ChallengeServiceImpl();
        this.workoutService = new WorkoutServiceImpl();
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Challenge History - " + loggedInUser.getName());
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("Your Challenge History & Progress");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        tableModel = new DefaultTableModel(
                new Object[]{
                        "Challenge",
                        "Status",
                        "Joined At",
                        "Completed At",
                        "Workouts Done",
                        "Total Minutes",
                        "Target W",
                        "Target Min",
                        "W% Achieved",
                        "M% Achieved"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton exportBtn = new JButton("Export CSV");
        JButton closeButton = new JButton("Close");

        exportBtn.addActionListener(e -> exportToCsv());
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(exportBtn);
        buttonPanel.add(closeButton);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(titleLabel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<UserChallenge> list = userChallengeService.getUserChallenges(loggedInUser.getId());
            for (UserChallenge uc : list) {
                Challenge c = challengeService.getById(uc.getChallengeId());
                String name = (c != null) ? c.getName() : ("ID " + uc.getChallengeId());

                Integer targetW = c != null ? c.getTargetWorkouts() : null;
                Integer targetM = c != null ? c.getTargetMinutes() : null;

                int workoutsDone = 0;
                int totalMinutes = 0;
                double wPercent = 0.0;
                double mPercent = 0.0;

                if (c != null) {
                    LocalDate from = c.getStartDate();
                    LocalDate to = c.getEndDate();

                    List<Workout> workouts =
                            workoutService.getWorkoutsForUserInRange(loggedInUser.getId(), from, to);

                    workoutsDone = workouts.size();
                    for (Workout w : workouts) {
                        totalMinutes += w.getDurationMinutes();
                    }

                    if (targetW != null && targetW > 0) {
                        wPercent = workoutsDone * 100.0 / targetW;
                    }
                    if (targetM != null && targetM > 0) {
                        mPercent = totalMinutes * 100.0 / targetM;
                    }
                }

                tableModel.addRow(new Object[]{
                        name,
                        uc.getStatus(),
                        uc.getJoinedAt(),
                        uc.getCompletedAt(),
                        workoutsDone,
                        totalMinutes,
                        targetW,
                        targetM,
                        String.format("%.1f%%", wPercent),
                        String.format("%.1f%%", mPercent)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading challenge history: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Challenge History as CSV");
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
            writer.write("Challenge,Status,JoinedAt,CompletedAt,WorkoutsDone,TotalMinutes,TargetW,TargetMin,WPercent,MPercent\n");
            int rows = tableModel.getRowCount();
            for (int i = 0; i < rows; i++) {
                for (int c = 0; c < tableModel.getColumnCount(); c++) {
                    Object v = tableModel.getValueAt(i, c);
                    String str = v == null ? "" : v.toString().replace("\"", "\"\"");
                    writer.write("\"" + str + "\"");
                    if (c < tableModel.getColumnCount() - 1) writer.write(",");
                }
                writer.write("\n");
            }
            ActivityLogger.log(loggedInUser.getId(), "Exported challenge history to CSV");
            JOptionPane.showMessageDialog(this, "Exported successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error exporting CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
