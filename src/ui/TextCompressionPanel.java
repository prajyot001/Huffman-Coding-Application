package ui;

import logic.HuffmanCoding;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Map;

public class TextCompressionPanel extends JPanel {

    private JTextArea inputArea, outputArea;
    private JTable codeTable;
    private JLabel statsLabel;
    private HuffmanCoding huffman;

    public TextCompressionPanel(AppContainer parent) {
        setLayout(new BorderLayout());
        setBackground(Theme.SECONDARY_COLOR);

        huffman = new HuffmanCoding();

        // ===== Header =====
        JLabel title = new JLabel(" Text Compression (Huffman Encoding)", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setOpaque(true);
        title.setBackground(Theme.PRIMARY_COLOR);
        title.setForeground(Theme.TEXT_COLOR);
        title.setPreferredSize(new Dimension(950, 70));
        add(title, BorderLayout.NORTH);

        // ===== Center Panels =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        centerPanel.setBackground(Theme.SECONDARY_COLOR);

        // --- Left Panel (Input) ---
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Input Text"));

        inputArea = new JTextArea();
        inputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        leftPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        centerPanel.add(leftPanel);

        // --- Right Panel (Encoded Output + Codes) ---
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Encoded Output & Codes"));

        outputArea = new JTextArea();
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        JPanel tablePanel = new JPanel(new BorderLayout());
        codeTable = new JTable(new DefaultTableModel(new Object[] { "Character", "Code" }, 0));
        tablePanel.add(new JScrollPane(codeTable), BorderLayout.CENTER);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Huffman Codes"));

        rightPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        rightPanel.add(tablePanel, BorderLayout.SOUTH);

        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);

        // ===== Stats Label =====
        statsLabel = new JLabel("Original: 0 bits | Encoded: 0 bits | Compression: 0%", SwingConstants.CENTER);
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statsLabel.setForeground(new Color(60, 60, 60));
        statsLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(statsLabel, BorderLayout.SOUTH);

        // ===== Bottom Buttons =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Theme.SECONDARY_COLOR);

        JButton compressBtn = new JButton(" Compress");
        JButton saveEncodedBtn = new JButton(" Save Encoded File");
        JButton clearBtn = new JButton(" Clear");
        JButton backBtn = new JButton(" Back");

        styleButton(compressBtn, Theme.BUTTON_COLOR);
        styleButton(saveEncodedBtn, new Color(46, 204, 113));
        styleButton(clearBtn, new Color(255, 167, 38));
        styleButton(backBtn, new Color(189, 189, 189));

        buttonPanel.add(compressBtn);
        buttonPanel.add(saveEncodedBtn);

        buttonPanel.add(clearBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.PAGE_END);

        // ===== Button Actions =====
        compressBtn.addActionListener(e -> compressText());
        saveEncodedBtn.addActionListener(e -> saveEncodedFile());
        clearBtn.addActionListener(e -> clearAll());
        backBtn.addActionListener(e -> parent.showHomePanel());
    }

    // ===== Style Buttons =====
    private void styleButton(JButton btn, Color color) {
        btn.setFont(Theme.BUTTON_FONT);
        btn.setBackground(color);
        btn.setForeground(Theme.TEXT_COLOR);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    // ===== Compress Action =====
    private void compressText() {
        String text = inputArea.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter some text first!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String encoded = huffman.encode(text);
            outputArea.setText(encoded);

            // Update table
            DefaultTableModel model = (DefaultTableModel) codeTable.getModel();
            model.setRowCount(0);
            for (Map.Entry<Character, String> entry : huffman.getHuffmanCodes().entrySet()) {
                model.addRow(new Object[] { entry.getKey(), entry.getValue() });
            }

            // Update stats
            int originalBits = text.length() * 8;
            int encodedBits = encoded.length();
            double compression = 100 - ((encodedBits / (double) originalBits) * 100);

            statsLabel.setText(String.format(
                    "Original: %d bits | Encoded: %d bits | Compression: %.2f%%",
                    originalBits, encodedBits, compression));

            JOptionPane.showMessageDialog(this, "Compression successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error during compression!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== Save Encoded File =====
    // ===== Save Encoded File (with embedded table) =====
    private void saveEncodedFile() {
        String encodedText = outputArea.getText().trim();
        if (encodedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No encoded text to save!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Encoded File (with Code Table)");
        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().endsWith(".txt"))
                file = new File(file.getAbsolutePath() + ".txt");

            try {
                huffman.saveEncodedWithTable(encodedText, file);
                JOptionPane.showMessageDialog(this, "File saved with embedded Huffman table!", "Saved",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

   

    // ===== Clear Fields =====
    private void clearAll() {
        inputArea.setText("");
        outputArea.setText("");
        ((DefaultTableModel) codeTable.getModel()).setRowCount(0);
        statsLabel.setText("Original: 0 bits | Encoded: 0 bits | Compression: 0%");
    }
}
