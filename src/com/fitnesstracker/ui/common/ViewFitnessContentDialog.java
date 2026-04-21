package com.fitnesstracker.ui.common;

import com.fitnesstracker.model.FitnessContent;

import javax.swing.*;
import java.awt.*;

public class ViewFitnessContentDialog extends JDialog {

    public ViewFitnessContentDialog(Frame owner, FitnessContent content) {
        super(owner, "View Fitness Content", true);
        initUI(content);
    }

    private void initUI(FitnessContent content) {
        setSize(600, 500);
        setLocationRelativeTo(getOwner());

        JLabel titleLabel = new JLabel(content.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel metaLabel = new JLabel(
                "Type: " + content.getContentType() +
                        " | Status: " + content.getStatus()
        );

        JTextArea bodyArea = new JTextArea();
        bodyArea.setEditable(false);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        bodyArea.setText(content.getContentBody());
        JScrollPane bodyScroll = new JScrollPane(bodyArea);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(metaLabel, BorderLayout.SOUTH);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(closeBtn);

        getContentPane().setLayout(new BorderLayout(10,10));
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(bodyScroll, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }
}
