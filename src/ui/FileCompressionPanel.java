package ui;

import logic.HuffmanCoding;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class FileCompressionPanel extends JPanel {

    private JTextArea fileContentArea;
    private JLabel fileLabel, statsLabel;
    private File selectedFile;
    private HuffmanCoding huffman;
    private String originalText = ""; // ✅ stores full file content with newlines

    public FileCompressionPanel(AppContainer parent) {
        setLayout(new BorderLayout());
        setBackground(Theme.SECONDARY_COLOR);

        huffman = new HuffmanCoding();

        // ===== Header =====
        JLabel title = new JLabel("📂 File Compression (Huffman Encoding)", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setOpaque(true);
        title.setBackground(Theme.PRIMARY_COLOR);
        title.setForeground(Theme.TEXT_COLOR);
        title.setPreferredSize(new Dimension(950, 70));
        add(title, BorderLayout.NORTH);

        // ===== File Info =====
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        filePanel.setBackground(Theme.SECONDARY_COLOR);
        fileLabel = new JLabel("No file selected.");
        fileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        fileLabel.setForeground(new Color(70, 70, 70));
        filePanel.add(fileLabel);
        add(filePanel, BorderLayout.NORTH);

        // ===== File Content Area =====
        fileContentArea = new JTextArea();
        fileContentArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        fileContentArea.setEditable(false);
        fileContentArea.setLineWrap(true);
        fileContentArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(fileContentArea);
        scroll.setBorder(BorderFactory.createTitledBorder("File Preview (First few lines)"));
        add(scroll, BorderLayout.CENTER);

        // ===== Stats Label =====
        statsLabel = new JLabel("Original: 0 bits | Encoded: 0 bits | Compression: 0%", SwingConstants.CENTER);
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statsLabel.setForeground(new Color(60, 60, 60));
        statsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(statsLabel, BorderLayout.SOUTH);

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Theme.SECONDARY_COLOR);

        JButton browseBtn = new JButton(" Browse File");
        JButton compressBtn = new JButton(" Compress File");
        JButton saveEncodedBtn = new JButton(" Save Encoded File");
        JButton backBtn = new JButton(" Back");

        styleButton(browseBtn, new Color(255, 167, 38));
        styleButton(compressBtn, Theme.BUTTON_COLOR);
        styleButton(saveEncodedBtn, new Color(46, 204, 113));
        styleButton(backBtn, new Color(189, 189, 189));

        buttonPanel.add(browseBtn);
        buttonPanel.add(compressBtn);
        buttonPanel.add(saveEncodedBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.PAGE_END);

        // ===== Button Actions =====
        browseBtn.addActionListener(e -> browseFile());
        compressBtn.addActionListener(e -> compressFile());
        saveEncodedBtn.addActionListener(e -> saveEncodedFile());
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

    // ===== Browse File =====
    private void browseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a Text File to Compress");
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            fileLabel.setText("Selected File: " + selectedFile.getName());

            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                StringBuilder textBuilder = new StringBuilder();
                StringBuilder preview = new StringBuilder();
                String line;
                int lineCount = 0;

                while ((line = reader.readLine()) != null) {
                    textBuilder.append(line).append("\n"); // ✅ Preserve line breaks
                    if (lineCount < 10) { // Preview only first 10 lines
                        preview.append(line).append("\n");
                    }
                    lineCount++;
                }

                originalText = textBuilder.toString();
                fileContentArea.setText(preview.toString());

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ===== Compress File =====
    private void compressFile() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please select a text file first!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (originalText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "File content is empty!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String encoded = huffman.encode(originalText);
            int originalBits = originalText.length() * 8;
            int encodedBits = encoded.length();
            double compression = 100 - ((encodedBits / (double) originalBits) * 100);

            statsLabel.setText(String.format(
                    "Original: %d bits | Encoded: %d bits | Compression: %.2f%%",
                    originalBits, encodedBits, compression));

            JOptionPane.showMessageDialog(this, "File compressed successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error compressing file!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ===== Save Encoded File =====
    private void saveEncodedFile() {
        if (huffman.getHuffmanCodes() == null || huffman.getHuffmanCodes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please compress a file first!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Encoded File (with Embedded Table)");
        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().endsWith(".txt"))
                file = new File(file.getAbsolutePath() + ".txt");

            try {
                // ✅ Use originalText, not preview area content
                String encodedText = huffman.encode(originalText);
                huffman.saveEncodedWithTable(encodedText, file);
                JOptionPane.showMessageDialog(this, "File saved with embedded Huffman table!", "Saved",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
