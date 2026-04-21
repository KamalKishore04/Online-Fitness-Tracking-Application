package com.fitnesstracker.ui.admin;

import com.fitnesstracker.dao.UserDao;
import com.fitnesstracker.dao.impl.UserDaoImpl;
import com.fitnesstracker.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserManagementFrame extends JFrame {

    private final UserDao userDao;
    private final User loggedInAdmin;

    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagementFrame(User loggedInAdmin) {
        this.loggedInAdmin = loggedInAdmin;
        this.userDao = new UserDaoImpl();
        initUI();
        loadUsers();
    }

    private void initUI() {
        setTitle("User Management - Online Fitness Tracker");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Top label
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Table
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Role", "Created At"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // table is read-only
            }
        };

        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Buttons
        JButton addButton = new JButton("Add User");
        JButton editButton = new JButton("Edit User");
        JButton deleteButton = new JButton("Delete User");
        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");

        addButton.addActionListener(e -> openAddDialog());
        editButton.addActionListener(e -> openEditDialog());
        deleteButton.addActionListener(e -> deleteSelectedUser());
        refreshButton.addActionListener(e -> loadUsers());
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        // Layout
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(titleLabel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadUsers() {
        tableModel.setRowCount(0); // clear rows
        try {
            List<User> users = userDao.findAllUsers();
            for (User u : users) {
                tableModel.addRow(new Object[]{
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole(),
                        u.getCreatedAt()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading users: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer getSelectedUserId() {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a user from the table.",
                    "No selection",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        Object value = tableModel.getValueAt(row, 0);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return Integer.parseInt(value.toString());
    }

    private void openAddDialog() {
        AddUserDialog dialog = new AddUserDialog(this, userDao);
        dialog.setVisible(true);
        // After dialog closes, reload
        loadUsers();
    }

    private void openEditDialog() {
        Integer userId = getSelectedUserId();
        if (userId == null) {
            return;
        }

        try {
            User user = userDao.findById(userId);
            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Selected user not found.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            EditUserDialog dialog = new EditUserDialog(this, userDao, user);
            dialog.setVisible(true);
            loadUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading user: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedUser() {
        Integer userId = getSelectedUserId();
        if (userId == null) {
            return;
        }

        // prevent admin from deleting themselves by mistake
        if (loggedInAdmin != null && loggedInAdmin.getId() == userId) {
            JOptionPane.showMessageDialog(this,
                    "You cannot delete the currently logged-in admin.",
                    "Operation not allowed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this user?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            userDao.deleteUser(userId);
            JOptionPane.showMessageDialog(this,
                    "User deleted successfully.");
            loadUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error deleting user: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
