package com.fitnesstracker.ui.user;

import com.fitnesstracker.model.FitnessContent;
import com.fitnesstracker.model.User;
import com.fitnesstracker.service.FitnessContentService;
import com.fitnesstracker.service.impl.FitnessContentServiceImpl;
import com.fitnesstracker.ui.common.ViewFitnessContentDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FitnessContentListFrame extends JFrame {

    private final User loggedInUser;
    private final FitnessContentService contentService;

    private JTable table;
    private DefaultTableModel tableModel;

    public FitnessContentListFrame(User user) {
        this.loggedInUser = user;
        this.contentService = new FitnessContentServiceImpl();
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Fitness Content - " + loggedInUser.getName());
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("Approved Fitness Content");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Title", "Type", "Description", "Submitted At"},
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
        JButton submitBtn = new JButton("Submit New");
        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn = new JButton("Close");

        viewBtn.addActionListener(e -> handleView());
        submitBtn.addActionListener(e -> openSubmitDialog());
        refreshBtn.addActionListener(e -> loadData());
        closeBtn.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(viewBtn);
        buttonPanel.add(submitBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);

        getContentPane().setLayout(new BorderLayout(10,10));
        getContentPane().add(titleLabel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<FitnessContent> list = contentService.getApprovedContent();
            for (FitnessContent c : list) {
                tableModel.addRow(new Object[]{
                        c.getId(),
                        c.getTitle(),
                        c.getContentType(),
                        c.getDescription(),
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
                    "Please select a content row.",
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
            FitnessContent content = contentService.getById(id);
            if (content == null) {
                JOptionPane.showMessageDialog(this,
                        "Content not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ViewFitnessContentDialog dialog =
                    new ViewFitnessContentDialog(this, content);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSubmitDialog() {
        SubmitFitnessContentDialog dialog =
                new SubmitFitnessContentDialog(this, loggedInUser, contentService);
        dialog.setVisible(true);
        loadData();
    }
}
