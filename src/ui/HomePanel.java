package ui;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel(AppContainer parent) {
        setLayout(new BorderLayout());
        setBackground(Theme.SECONDARY_COLOR);

        // ===== Header =====
        JLabel title = new JLabel("🧠 Text Compression using Huffman Coding", SwingConstants.CENTER);
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
        subtitle.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        centerPanel.add(subtitle);

        // ===== Buttons Layout =====
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 40, 25));
        buttonPanel.setBackground(Theme.SECONDARY_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 200, 60, 200));

        JButton textCompressBtn = new JButton("🔹 Text Compression");
        JButton textDecompressBtn = new JButton("🔸 Text Decompression");
        JButton fileCompressBtn = new JButton("📂 File Compression");
        JButton fileDecompressBtn = new JButton("📑 File Decompression");

        styleButton(textCompressBtn, Theme.BUTTON_COLOR);
        styleButton(textDecompressBtn, new Color(255, 167, 38));
        styleButton(fileCompressBtn, new Color(46, 204, 113));
        styleButton(fileDecompressBtn, new Color(121, 134, 203));

        textCompressBtn.addActionListener(e -> parent.showTextCompressionPanel());
        textDecompressBtn.addActionListener(e -> parent.showTextDecompressionPanel());
        fileCompressBtn.addActionListener(e -> parent.showFileCompressionPanel());
        fileDecompressBtn.addActionListener(e -> parent.showFileDecompressionPanel());

        buttonPanel.add(textCompressBtn);
        buttonPanel.add(textDecompressBtn);
        buttonPanel.add(fileCompressBtn);
        buttonPanel.add(fileDecompressBtn);

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
        btn.setFont(Theme.BUTTON_FONT);
        btn.setBackground(color);
        btn.setForeground(Theme.TEXT_COLOR);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        btn.setOpaque(true);

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
