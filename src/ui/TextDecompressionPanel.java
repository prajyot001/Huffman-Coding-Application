package ui;

import logic.HuffmanCoding;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TextDecompressionPanel extends JPanel {

    private JTextArea encodedArea, codeTableArea, outputArea;
    private HuffmanCoding huffman;

    public TextDecompressionPanel(AppContainer parent) {
        setLayout(new BorderLayout());
        setBackground(Theme.SECONDARY_COLOR);

        huffman = new HuffmanCoding();

        // ===== Header =====
        JLabel title = new JLabel(" Text Decompression (Huffman Decoding)", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setOpaque(true);
        title.setBackground(Theme.PRIMARY_COLOR);
        title.setForeground(Theme.TEXT_COLOR);
        title.setPreferredSize(new Dimension(950, 70));
        add(title, BorderLayout.NORTH);

        // ===== Center Layout =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        centerPanel.setBackground(Theme.SECONDARY_COLOR);

        // --- Left Panel (Encoded Input) ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Encoded Binary Text"));
        encodedArea = new JTextArea();
        encodedArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        encodedArea.setLineWrap(true);
        encodedArea.setWrapStyleWord(true);
        leftPanel.add(new JScrollPane(encodedArea), BorderLayout.CENTER);
        centerPanel.add(leftPanel);

        // --- Middle Panel (Code Table) ---
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBackground(Color.WHITE);
        middlePanel.setBorder(BorderFactory.createTitledBorder("Huffman Code Table"));
        codeTableArea = new JTextArea();
        codeTableArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        codeTableArea.setText("Example:\nH:110\nE:111\nL:0\nO:10");
        codeTableArea.setLineWrap(true);
        codeTableArea.setWrapStyleWord(true);
        middlePanel.add(new JScrollPane(codeTableArea), BorderLayout.CENTER);
        centerPanel.add(middlePanel);

        // --- Right Panel (Decoded Output) ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Decoded Text Output"));
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        rightPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        // ===== Bottom Buttons =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Theme.SECONDARY_COLOR);

        JButton decodeBtn = new JButton(" Decode");
        JButton saveBtn = new JButton(" Save Decoded File");
        JButton clearBtn = new JButton(" Clear");
        JButton backBtn = new JButton(" Back");

        styleButton(decodeBtn, Theme.ACCENT_COLOR);
        styleButton(saveBtn, new Color(46, 204, 113));
        styleButton(clearBtn, new Color(255, 167, 38));
        styleButton(backBtn, new Color(189, 189, 189));

        buttonPanel.add(decodeBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Button Actions =====
        decodeBtn.addActionListener(e -> decodeText());
        saveBtn.addActionListener(e -> saveDecodedText());
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

    // ===== Decode from Text Areas =====
    private void decodeText() {
    String encodedText = encodedArea.getText().trim();
    String codeTableText = codeTableArea.getText().trim();

    if (encodedText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter encoded binary text!", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (codeTableText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please provide the Huffman code table!", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        // Step 1: Parse Huffman code table
        Map<Character, String> codeMap = new HashMap<>();
        String[] lines = codeTableText.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || !line.contains(":")) continue;
            String[] parts = line.split(":");
            if (parts.length == 2 && !parts[0].isEmpty()) {
                String key = parts[0].equals("\\n") ? "\n" : parts[0];
                codeMap.put(key.charAt(0), parts[1].trim());
            }
        }

        if (codeMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid code table format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 2: Build reverse map for decoding
        Map<String, Character> reverseMap = new HashMap<>();
        for (Map.Entry<Character, String> e : codeMap.entrySet()) {
            reverseMap.put(e.getValue(), e.getKey());
        }

        // Step 3: Clean encoded input (remove invalid chars)
        encodedText = encodedText.replaceAll("[^01\\n]", ""); // ✅ keep only bits and newlines

        // Step 4: Decode each line separately
        String[] encodedLines = encodedText.split("\\r?\\n");
        StringBuilder decoded = new StringBuilder();

        for (String encodedLine : encodedLines) {
            if (encodedLine.trim().isEmpty()) {
                decoded.append("\n");
                continue;
            }

            StringBuilder temp = new StringBuilder();
            for (char bit : encodedLine.toCharArray()) {
                temp.append(bit);
                if (reverseMap.containsKey(temp.toString())) {
                    decoded.append(reverseMap.get(temp.toString()));
                    temp.setLength(0);
                }
            }
            decoded.append("\n");
        }

        outputArea.setText(decoded.toString());
        JOptionPane.showMessageDialog(this, "Decoding successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error during decoding!", "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}


    // ===== Save Decoded Text =====
    private void saveDecodedText() {
        String decodedText = outputArea.getText().trim();
        if (decodedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No decoded text to save!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Decoded File");
        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(decodedText);
                JOptionPane.showMessageDialog(this, "Decoded text saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ===== Clear All =====
    private void clearAll() {
        encodedArea.setText("");
        codeTableArea.setText("Example:\nH:110\nE:111\nL:0\nO:10");
        outputArea.setText("");
    }
}
