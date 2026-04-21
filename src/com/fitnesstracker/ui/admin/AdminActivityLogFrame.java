package com.fitnesstracker.ui.admin;

import com.fitnesstracker.model.ActivityLog;
import com.fitnesstracker.model.User;
import com.fitnesstracker.service.ActivityLogService;
import com.fitnesstracker.service.impl.ActivityLogServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AdminActivityLogFrame extends JFrame {

    private final User adminUser;
    private final ActivityLogService logService;

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField userIdField;
    private JTextField fromDateField;
    private JTextField toDateField;

    public AdminActivityLogFrame(User adminUser) {
        this.adminUser = adminUser;
        this.logService = new ActivityLogServiceImpl();
        initUI();
        loadAll();
    }

    private void initUI() {
        setTitle("System Activity Logs - Admin");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("System Activity Logs");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("User ID:"));
        userIdField = new JTextField(5);
        filterPanel.add(userIdField);

        filterPanel.add(new JLabel("From (yyyy-MM-dd):"));
        fromDateField = new JTextField(10);
        filterPanel.add(fromDateField);

        filterPanel.add(new JLabel("To:"));
        toDateField = new JTextField(10);
        filterPanel.add(toDateField);

        JButton filterBtn = new JButton("Filter");
        JButton clearBtn = new JButton("Clear");
        filterPanel.add(filterBtn);
        filterPanel.add(clearBtn);

        filterBtn.addActionListener(e -> applyFilter());
        clearBtn.addActionListener(e -> {
            userIdField.setText("");
            fromDateField.setText("");
            toDateField.setText("");
            loadAll();
        });

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "User ID", "Action", "Action Time"},
                0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(closeBtn);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(titleLabel, BorderLayout.NORTH);
        getContentPane().add(filterPanel, BorderLayout.BEFORE_FIRST_LINE);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadAll() {
        tableModel.setRowCount(0);
        try {
            List<ActivityLog> list = logService.getAll();
            for (ActivityLog l : list) {
                tableModel.addRow(new Object[]{
                        l.getId(),
                        l.getUserId(),
                        l.getAction(),
                        l.getActionTime()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading logs: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilter() {
        tableModel.setRowCount(0);

        try {
            String userIdText = userIdField.getText().trim();
            String fromText = fromDateField.getText().trim();
            String toText = toDateField.getText().trim();

            Integer uid = null;
            if (!userIdText.isEmpty()) {
                uid = Integer.parseInt(userIdText);
            }

            List<ActivityLog> list;
            if (!fromText.isEmpty() && !toText.isEmpty()) {
                LocalDate from = LocalDate.parse(fromText);
                LocalDate to = LocalDate.parse(toText);
                list = logService.getByDateRange(
                        from.atStartOfDay(),
                        to.atTime(23, 59, 59)
                );
            } else if (uid != null) {
                list = logService.getByUser(uid);
            } else {
                list = logService.getAll();
            }

            for (ActivityLog l : list) {
                if (uid != null && l.getUserId() != null && !uid.equals(l.getUserId())) {
                    continue;
                }
                tableModel.addRow(new Object[]{
                        l.getId(),
                        l.getUserId(),
                        l.getAction(),
                        l.getActionTime()
                });
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "User ID must be numeric.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error filtering logs: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
