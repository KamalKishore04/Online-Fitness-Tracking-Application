package com.fitnesstracker.ui.admin;

import com.fitnesstracker.model.SystemSetting;
import com.fitnesstracker.model.User;
import com.fitnesstracker.service.SystemSettingService;
import com.fitnesstracker.service.impl.SystemSettingServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminSystemSettingsFrame extends JFrame {

    private final User adminUser;
    private final SystemSettingService settingService;

    private JTable table;
    private DefaultTableModel tableModel;

    public AdminSystemSettingsFrame(User adminUser) {
        this.adminUser = adminUser;
        this.settingService = new SystemSettingServiceImpl();
        initUI();
        loadSettings();
    }

    private void initUI() {
        setTitle("System Settings - Admin");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("System Settings");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        tableModel = new DefaultTableModel(
                new Object[]{"Key", "Value", "Updated At"},
                0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                // only allow editing value field (column 1)
                return c == 1;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton saveBtn = new JButton("Save Changes");
        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn = new JButton("Close");

        saveBtn.addActionListener(e -> saveSettings());
        refreshBtn.addActionListener(e -> loadSettings());
        closeBtn.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(titleLabel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadSettings() {
        tableModel.setRowCount(0);
        try {
            List<SystemSetting> list = settingService.getAllSettings();
            for (SystemSetting s : list) {
                tableModel.addRow(new Object[]{
                        s.getSettingKey(),
                        s.getSettingValue(),
                        s.getUpdatedAt()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading settings: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveSettings() {
        try {
            int rows = tableModel.getRowCount();
            for (int i = 0; i < rows; i++) {
                String key = tableModel.getValueAt(i, 0).toString();
                String value = tableModel.getValueAt(i, 1) == null ? "" :
                        tableModel.getValueAt(i, 1).toString();
                settingService.updateSetting(key, value);
            }
            JOptionPane.showMessageDialog(this, "Settings updated.");
            loadSettings();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving settings: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
