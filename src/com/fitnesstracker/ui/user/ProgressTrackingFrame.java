package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.User;
import com.fitnesstracker.model.Workout;
import com.fitnesstracker.service.WorkoutService;
import com.fitnesstracker.service.impl.WorkoutServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressTrackingFrame extends JFrame {

    private final User loggedInUser;
    private final WorkoutService workoutService;

    private JTextField fromDateField;
    private JTextField toDateField;

    private JLabel totalWorkoutsLabel;
    private JLabel totalMinutesLabel;
    private JLabel avgMinutesLabel;

    private JTable workoutTable;
    private DefaultTableModel workoutTableModel;

    private JTable typeSummaryTable;
    private DefaultTableModel typeSummaryTableModel;

    private BarChartPanel barChartPanel;

    public ProgressTrackingFrame(User user) {
        this.loggedInUser = user;
        this.workoutService = new WorkoutServiceImpl();
        initUI();
        // default: last 30 days
        LocalDate today = LocalDate.now();
        fromDateField.setText(today.minusDays(30).toString());
        toDateField.setText(today.toString());
        loadData();
    }

    private void initUI() {
        setTitle("Progress Tracking - " + loggedInUser.getName());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // === TOP FILTER PANEL ===
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        filterPanel.add(new JLabel("From (yyyy-MM-dd):"));
        fromDateField = new JTextField(10);
        filterPanel.add(fromDateField);

        filterPanel.add(new JLabel("To:"));
        toDateField = new JTextField(10);
        filterPanel.add(toDateField);

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> loadData());
        filterPanel.add(loadButton);

        // === SUMMARY PANEL ===
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        totalWorkoutsLabel = new JLabel("Total Workouts: 0");
        totalMinutesLabel = new JLabel("Total Minutes: 0");
        avgMinutesLabel = new JLabel("Avg Minutes/Workout: 0");

        summaryPanel.add(totalWorkoutsLabel);
        summaryPanel.add(Box.createHorizontalStrut(20));
        summaryPanel.add(totalMinutesLabel);
        summaryPanel.add(Box.createHorizontalStrut(20));
        summaryPanel.add(avgMinutesLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(filterPanel, BorderLayout.NORTH);
        topPanel.add(summaryPanel, BorderLayout.SOUTH);

        // === WORKOUT TABLE ===
        workoutTableModel = new DefaultTableModel(
                new Object[]{"Date", "Type", "Duration (min)", "Intensity", "Notes"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        workoutTable = new JTable(workoutTableModel);
        JScrollPane workoutScroll = new JScrollPane(workoutTable);

        // === TYPE SUMMARY TABLE ===
        typeSummaryTableModel = new DefaultTableModel(
                new Object[]{"Workout Type", "Total Minutes"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        typeSummaryTable = new JTable(typeSummaryTableModel);
        JScrollPane typeSummaryScroll = new JScrollPane(typeSummaryTable);

        // === BAR CHART PANEL ===
        barChartPanel = new BarChartPanel();

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(typeSummaryScroll, BorderLayout.CENTER);
        rightPanel.add(barChartPanel, BorderLayout.SOUTH);

        JSplitPane centerSplit = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                workoutScroll,
                rightPanel
        );
        centerSplit.setResizeWeight(0.6);

        // === MAIN LAYOUT ===
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(centerSplit, BorderLayout.CENTER);
    }

    private void loadData() {
        LocalDate from;
        LocalDate to;
        try {
            from = LocalDate.parse(fromDateField.getText().trim());
            to = LocalDate.parse(toDateField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Use yyyy-MM-dd.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (from.isAfter(to)) {
            JOptionPane.showMessageDialog(this,
                    "From date cannot be after To date.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Workout> workouts =
                    workoutService.getWorkoutsForUserInRange(loggedInUser.getId(), from, to);

            // Fill workouts table
            workoutTableModel.setRowCount(0);
            int totalMinutes = 0;
            Map<String, Integer> minutesByType = new HashMap<>();

            for (Workout w : workouts) {
                workoutTableModel.addRow(new Object[]{
                        w.getWorkoutDate(),
                        w.getWorkoutType(),
                        w.getDurationMinutes(),
                        w.getIntensity(),
                        w.getNotes()
                });

                totalMinutes += w.getDurationMinutes();

                String type = w.getWorkoutType();
                minutesByType.put(type,
                        minutesByType.getOrDefault(type, 0) + w.getDurationMinutes());
            }

            int totalWorkouts = workouts.size();
            double avgMinutes = totalWorkouts == 0
                    ? 0
                    : (double) totalMinutes / totalWorkouts;

            totalWorkoutsLabel.setText("Total Workouts: " + totalWorkouts);
            totalMinutesLabel.setText("Total Minutes: " + totalMinutes);
            avgMinutesLabel.setText(String.format("Avg Minutes/Workout: %.1f", avgMinutes));

            // Fill type summary table
            typeSummaryTableModel.setRowCount(0);
            for (Map.Entry<String, Integer> entry : minutesByType.entrySet()) {
                typeSummaryTableModel.addRow(new Object[]{
                        entry.getKey(),
                        entry.getValue()
                });
            }

            // Update bar chart
            barChartPanel.setData(minutesByType);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading progress: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==== INNER CLASS: SIMPLE BAR CHART ====
    private static class BarChartPanel extends JPanel {

        private Map<String, Integer> data = new HashMap<>();

        public BarChartPanel() {
            setPreferredSize(new Dimension(400, 200));
        }

        public void setData(Map<String, Integer> data) {
            this.data = data != null ? data : new HashMap<>();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (data.isEmpty()) {
                g.drawString("No data to display", 10, 20);
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            int padding = 30;
            int chartWidth = width - 2 * padding;
            int chartHeight = height - 2 * padding;

            int max = data.values().stream().max(Integer::compareTo).orElse(1);

            int barCount = data.size();
            int barWidth = Math.max(20, chartWidth / (barCount * 2));

            int index = 0;

            // axes
            g2.drawLine(padding, padding, padding, padding + chartHeight);
            g2.drawLine(padding, padding + chartHeight, padding + chartWidth, padding + chartHeight);

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                String type = entry.getKey();
                int value = entry.getValue();

                int x = padding + (2 * index + 1) * barWidth;
                int barHeight = (int) ((value / (double) max) * (chartHeight - 20));

                int y = padding + chartHeight - barHeight;

                g2.fillRect(x, y, barWidth, barHeight);
                g2.drawString(String.valueOf(value), x, y - 5);

                // type label
                g2.drawString(type, x, padding + chartHeight + 15);

                index++;
            }
        }
    }
}
