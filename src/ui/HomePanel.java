package ui;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel(AppContainer parent) {
        setLayout(new BorderLayout());
        setBackground(Theme.SECONDARY_COLOR);

        // ===== Header =====
        JLabel title = new JLabel(" Text Compression using Huffman Coding", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setOpaque(true);
        title.setBackground(Theme.PRIMARY_COLOR);
        title.setForeground(Theme.TEXT_COLOR);
        title.setPreferredSize(new Dimension(900, 70));
        add(title, BorderLayout.NORTH);

        // ===== Center Section =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Theme.SECONDARY_COLOR);

        JLabel subtitle = new JLabel("Select Operation", SwingConstants.CENTER);
        subtitle.setFont(Theme.SUBTITLE_FONT);
        subtitle.setForeground(new Color(50, 50, 50));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        centerPanel.add(subtitle);

        // ===== Buttons Layout (VERTICAL STACK) =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Theme.SECONDARY_COLOR);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 350, 40, 350));

        JButton textCompressBtn = new JButton(" Text Compression");
        JButton textDecompressBtn = new JButton(" Text Decompression");
        JButton fileCompressBtn = new JButton(" File Compression");
        JButton fileDecompressBtn = new JButton(" File Decompression");

        styleButton(textCompressBtn, Theme.BUTTON_COLOR);
        styleButton(textDecompressBtn, new Color(255, 167, 38));
        styleButton(fileCompressBtn, new Color(46, 204, 113));
        styleButton(fileDecompressBtn, new Color(121, 134, 203));

        Dimension btnSize = new Dimension(250, 60); // ✅ Balanced vertical size
        textCompressBtn.setMaximumSize(btnSize);
        textDecompressBtn.setMaximumSize(btnSize);
        fileCompressBtn.setMaximumSize(btnSize);
        fileDecompressBtn.setMaximumSize(btnSize);

        // Add spacing between buttons
        buttonPanel.add(textCompressBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(textDecompressBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(fileCompressBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(fileDecompressBtn);

        // Button Actions
        textCompressBtn.addActionListener(e -> parent.showTextCompressionPanel());
        textDecompressBtn.addActionListener(e -> parent.showTextDecompressionPanel());
        fileCompressBtn.addActionListener(e -> parent.showFileCompressionPanel());
        fileDecompressBtn.addActionListener(e -> parent.showFileDecompressionPanel());

        // Add button panel to center
        centerPanel.add(buttonPanel);
        add(centerPanel, BorderLayout.CENTER);

        // ===== Footer =====
        JLabel footer = new JLabel("Developed by Prajyot (Engineering Student)", SwingConstants.CENTER);
        footer.setFont(Theme.FOOTER_FONT);
        footer.setForeground(Theme.FOOTER_COLOR);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footer, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1, true),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        btn.setOpaque(true);

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
    }
}
