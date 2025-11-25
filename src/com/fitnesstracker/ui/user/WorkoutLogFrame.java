package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.User;
import com.fitnesstracker.model.Workout;
import com.fitnesstracker.service.WorkoutService;
import com.fitnesstracker.service.impl.WorkoutServiceImpl;
import com.fitnesstracker.logging.ActivityLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;

public class WorkoutLogFrame extends JFrame {

    private final User loggedInUser;
    private final WorkoutService workoutService;

    private JTable workoutTable;
    private DefaultTableModel tableModel;

    public WorkoutLogFrame(User user) {
        this.loggedInUser = user;
        this.workoutService = new WorkoutServiceImpl();
        initUI();
        loadWorkouts();
    }

    private void initUI() {
        setTitle("Workout Log - " + loggedInUser.getName());
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("Workout Log for " + loggedInUser.getName());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Date", "Type", "Duration (min)", "Intensity", "Notes"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        workoutTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(workoutTable);

        JButton addButton = new JButton("Add Workout");
        JButton editButton = new JButton("Edit Workout");
        JButton deleteButton = new JButton("Delete Workout");
        JButton refreshButton = new JButton("Refresh");
        JButton exportButton = new JButton("Export CSV");
        JButton closeButton = new JButton("Close");

        addButton.addActionListener(e -> openAddDialog());
        editButton.addActionListener(e -> openEditDialog());
        deleteButton.addActionListener(e -> deleteSelectedWorkout());
        refreshButton.addActionListener(e -> loadWorkouts());
        exportButton.addActionListener(e -> exportToCsv());
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(closeButton);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(titleLabel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadWorkouts() {
        tableModel.setRowCount(0);
        try {
            List<Workout> list = workoutService.getWorkoutsForUser(loggedInUser.getId());
            for (Workout w : list) {
                tableModel.addRow(new Object[]{
                        w.getId(),
                        w.getWorkoutDate(),
                        w.getWorkoutType(),
                        w.getDurationMinutes(),
                        w.getIntensity(),
                        w.getNotes()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading workouts: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer getSelectedWorkoutId() {
        int row = workoutTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a workout row.",
                    "No selection",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        Object value = tableModel.getValueAt(row, 0);
        return Integer.parseInt(value.toString());
    }

    private void openAddDialog() {
        AddWorkoutDialog dialog = new AddWorkoutDialog(this, loggedInUser, workoutService);
        dialog.setVisible(true);
        loadWorkouts();
    }

    private void openEditDialog() {
        Integer id = getSelectedWorkoutId();
        if (id == null) return;

        int row = workoutTable.getSelectedRow();

        Workout w = new Workout();
        w.setId(id);
        w.setUserId(loggedInUser.getId());
        w.setWorkoutDate(LocalDate.parse(tableModel.getValueAt(row, 1).toString()));
        w.setWorkoutType(tableModel.getValueAt(row, 2).toString());
        w.setDurationMinutes(Integer.parseInt(tableModel.getValueAt(row, 3).toString()));
        w.setIntensity(tableModel.getValueAt(row, 4).toString());
        Object notesVal = tableModel.getValueAt(row, 5);
        w.setNotes(notesVal == null ? null : notesVal.toString());

        EditWorkoutDialog dialog = new EditWorkoutDialog(this, workoutService, w);
        dialog.setVisible(true);
        loadWorkouts();
    }

    private void deleteSelectedWorkout() {
        Integer id = getSelectedWorkoutId();
        if (id == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected workout?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            workoutService.deleteWorkout(id);
            ActivityLogger.log(loggedInUser.getId(), "Deleted workout id=" + id);
            JOptionPane.showMessageDialog(this, "Workout deleted.");
            loadWorkouts();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error deleting workout: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Workouts as CSV");
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
            writer.write("ID,Date,Type,Duration,Intensity,Notes\n");
            int rows = tableModel.getRowCount();
            for (int i = 0; i < rows; i++) {
                writer.write(tableModel.getValueAt(i, 0) + ",");
                writer.write(tableModel.getValueAt(i, 1) + ",");
                writer.write("\"" + tableModel.getValueAt(i, 2) + "\",");
                writer.write(tableModel.getValueAt(i, 3) + ",");
                writer.write(tableModel.getValueAt(i, 4) + ",");
                Object notes = tableModel.getValueAt(i, 5);
                String notesStr = notes == null ? "" : notes.toString().replace("\"", "\"\"");
                writer.write("\"" + notesStr + "\"\n");
            }
            ActivityLogger.log(loggedInUser.getId(), "Exported workouts to CSV");
            JOptionPane.showMessageDialog(this, "Exported successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error exporting CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
