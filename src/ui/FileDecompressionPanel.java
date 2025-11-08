package ui;

import logic.HuffmanCoding;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileDecompressionPanel extends JPanel {

    private JTextArea previewArea;
    private JLabel encodedLabel, codeTableLabel;
    private File encodedFile, codeTableFile;
    private HuffmanCoding huffman;

    public FileDecompressionPanel(AppContainer parent) {
        setLayout(new BorderLayout());
        setBackground(Theme.SECONDARY_COLOR);
        huffman = new HuffmanCoding();

        // ===== Header =====
        JLabel title = new JLabel(" File Decompression (Huffman Decoding)", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setOpaque(true);
        title.setBackground(Theme.PRIMARY_COLOR);
        title.setForeground(Theme.TEXT_COLOR);
        title.setPreferredSize(new Dimension(950, 70));
        add(title, BorderLayout.NORTH);

        // ===== Info Panel =====
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        infoPanel.setBackground(Theme.SECONDARY_COLOR);

        encodedLabel = new JLabel("Encoded File: None selected");
        encodedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        encodedLabel.setForeground(new Color(60, 60, 60));

        codeTableLabel = new JLabel("Code Table: None selected");
        codeTableLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        codeTableLabel.setForeground(new Color(60, 60, 60));

        infoPanel.add(encodedLabel);
        infoPanel.add(codeTableLabel);
        add(infoPanel, BorderLayout.NORTH);

        // ===== Center Preview =====
        previewArea = new JTextArea();
        previewArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        previewArea.setEditable(false);
        previewArea.setLineWrap(true);
        previewArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(previewArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Decoded Text Preview"));
        add(scroll, BorderLayout.CENTER);

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Theme.SECONDARY_COLOR);

       
        JButton decodeBtn = new JButton(" Decode");
        JButton saveBtn = new JButton(" Save Decoded File");
        JButton backBtn = new JButton(" Back");

        styleButton(decodeBtn, Theme.ACCENT_COLOR);
        styleButton(saveBtn, new Color(46, 204, 113));
        styleButton(backBtn, new Color(189, 189, 189));
        
        buttonPanel.add(decodeBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Button Actions =====
        decodeBtn.addActionListener(e -> decodeFile());
        saveBtn.addActionListener(e -> saveDecodedFile());
        backBtn.addActionListener(e -> parent.showHomePanel());
    }

    // ===== Style Button =====
    private void styleButton(JButton btn, Color color) {
        btn.setFont(Theme.BUTTON_FONT);
        btn.setBackground(color);
        btn.setForeground(Theme.TEXT_COLOR);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(190, 45));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    // ===== Load Encoded File =====
    private void loadEncodedFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Encoded File");
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            encodedFile = chooser.getSelectedFile();
            encodedLabel.setText("Encoded File: " + encodedFile.getName());
            JOptionPane.showMessageDialog(this, "Encoded file loaded successfully!");
        }
    }

    // ===== Decode File =====
    private void decodeFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Encoded File (with Embedded Huffman Table)");
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION)
            return;

        File file = chooser.getSelectedFile();

        try {
            Map<String, String> data = huffman.loadEmbeddedEncodedFile(file);
            String encoded = data.get("encoded");

            Map<String, Character> reverseMap = new HashMap<>();
            for (Map.Entry<Character, String> entry : huffman.getHuffmanCodes().entrySet()) {
                reverseMap.put(entry.getValue(), entry.getKey());
            }

            StringBuilder decoded = new StringBuilder();
            StringBuilder temp = new StringBuilder();

            for (char bit : encoded.toCharArray()) {
                temp.append(bit);
                if (reverseMap.containsKey(temp.toString())) {
                    decoded.append(reverseMap.get(temp.toString()));
                    temp.setLength(0);
                }
            }

            previewArea.setText(decoded.toString());
            JOptionPane.showMessageDialog(this, "Decoded successfully from embedded file!");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading embedded file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== Save Decoded File =====
    private void saveDecodedFile() {
        String decodedText = previewArea.getText().trim();
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
                JOptionPane.showMessageDialog(this, "Decoded file saved successfully!", "Saved",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving decoded file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
