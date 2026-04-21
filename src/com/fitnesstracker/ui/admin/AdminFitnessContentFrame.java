package com.fitnesstracker.ui.admin;

import com.fitnesstracker.model.FitnessContent;
import com.fitnesstracker.model.User;
import com.fitnesstracker.service.FitnessContentService;
import com.fitnesstracker.service.impl.FitnessContentServiceImpl;
import com.fitnesstracker.ui.common.ViewFitnessContentDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminFitnessContentFrame extends JFrame {

    private final User adminUser;
    private final FitnessContentService contentService;

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilter;

    public AdminFitnessContentFrame(User adminUser) {
        this.adminUser = adminUser;
        this.contentService = new FitnessContentServiceImpl();
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Fitness Content Moderation - Admin");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("Manage Fitness Content");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Filter Status:"));
        statusFilter = new JComboBox<>(new String[]{"ALL", "PENDING", "APPROVED", "REJECTED"});
        JButton applyFilterBtn = new JButton("Apply");
        applyFilterBtn.addActionListener(e -> loadData());
        topPanel.add(statusFilter);
        topPanel.add(applyFilterBtn);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Title", "Type", "Status", "Submitted By", "Submitted At"},
                0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton viewBtn = new JButton("View");
        JButton approveBtn = new JButton("Approve");
        JButton rejectBtn = new JButton("Reject");
        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn = new JButton("Close");

        viewBtn.addActionListener(e -> handleView());
        approveBtn.addActionListener(e -> handleApproval(true));
        rejectBtn.addActionListener(e -> handleApproval(false));
        refreshBtn.addActionListener(e -> loadData());
        closeBtn.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(viewBtn);
        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);

        getContentPane().setLayout(new BorderLayout(10,10));
        getContentPane().add(titleLabel, BorderLayout.NORTH);
        getContentPane().add(topPanel, BorderLayout.BEFORE_FIRST_LINE);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            String filter = (String) statusFilter.getSelectedItem();
            List<FitnessContent> list;
            if ("PENDING".equals(filter)) {
                list = contentService.getPendingContent();
            } else if ("APPROVED".equals(filter)) {
                list = contentService.getApprovedContent();
            } else if ("REJECTED".equals(filter)) {
                // NO direct method, use all and filter, or add DAO method.
                list = contentService.getAllContent().stream()
                        .filter(c -> "REJECTED".equals(c.getStatus()))
                        .toList();
            } else {
                list = contentService.getAllContent();
            }

            for (FitnessContent c : list) {
                tableModel.addRow(new Object[]{
                        c.getId(),
                        c.getTitle(),
                        c.getContentType(),
                        c.getStatus(),
                        c.getSubmittedBy(),
                        c.getSubmittedAt()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading content: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer getSelectedId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a row.",
                    "No selection",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        Object val = tableModel.getValueAt(row, 0);
        return Integer.parseInt(val.toString());
    }

    private void handleView() {
        Integer id = getSelectedId();
        if (id == null) return;

        try {
            FitnessContent c = contentService.getById(id);
            if (c == null) {
                JOptionPane.showMessageDialog(this,
                        "Content not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ViewFitnessContentDialog dialog =
                    new ViewFitnessContentDialog(this, c);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleApproval(boolean approve) {
        Integer id = getSelectedId();
        if (id == null) return;

        try {
            if (approve) {
                contentService.approveContent(id, adminUser.getId());
                JOptionPane.showMessageDialog(this, "Content approved.");
            } else {
                contentService.rejectContent(id, adminUser.getId());
                JOptionPane.showMessageDialog(this, "Content rejected.");
            }
            loadData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

