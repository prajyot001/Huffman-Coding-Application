package logic;

import java.util.*;
import java.io.*;

/**
 * HuffmanCoding.java
 * Implements Huffman Encoding and Decoding logic for text compression.
 * Author: Prajyot (Engineering Student)
 */

public class HuffmanCoding {

    // ===== Inner Class for Huffman Tree Node =====
    private static class Node implements Comparable<Node> {
        char ch;
        int freq;
        Node left, right;

        Node(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
        }

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return (left == null && right == null);
        }

        @Override
        public int compareTo(Node other) {
            // Stable ordering: first by frequency, then alphabetically
            int freqCompare = Integer.compare(this.freq, other.freq);
            if (freqCompare == 0) {
                return Character.compare(this.ch, other.ch);
            }
            return freqCompare;
        }
    }

    // ===== Main Data Structures =====
    private Map<Character, String> huffmanCode;
    private Node root;

    // ===== Encode Text =====
    public String encode(String text) {
        if (text == null || text.isEmpty()) return "";

        // Step 1: Frequency map
        Map<Character, Integer> freqMap = buildFrequencyMap(text);

        // Step 2: Build Huffman tree
        root = buildHuffmanTree(freqMap);

        // Step 3: Generate Huffman codes
        huffmanCode = new HashMap<>();
        generateCodes(root, "", huffmanCode);

        // Step 4: Encode text
        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            encoded.append(huffmanCode.get(c));
        }

        return encoded.toString();
    }

    // ===== Decode Text =====
    public String decode(String encodedText) {
        if (root == null || encodedText == null || encodedText.isEmpty()) return "";

        StringBuilder decoded = new StringBuilder();
        Node current = root;

        for (char bit : encodedText.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;

            if (current.isLeaf()) {
                decoded.append(current.ch);
                current = root;
            }
        }

        return decoded.toString();
    }

    // ===== Frequency Map =====
    private Map<Character, Integer> buildFrequencyMap(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        return freqMap;
    }

    // ===== Build Huffman Tree =====
    private Node buildHuffmanTree(Map<Character, Integer> freqMap) {
        PriorityQueue<Node> pq = new PriorityQueue<>();

        for (var entry : freqMap.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.add(parent);
        }

        return pq.peek(); // root
    }

    // ===== Generate Huffman Codes (recursive) =====
    private void generateCodes(Node node, String code, Map<Character, String> map) {
        if (node == null) return;
        if (node.isLeaf()) {
            map.put(node.ch, code.length() > 0 ? code : "0"); // Handle single-char text
        }
        generateCodes(node.left, code + '0', map);
        generateCodes(node.right, code + '1', map);
    }

    // ===== Getters =====
    public Map<Character, String> getHuffmanCodes() {
        return huffmanCode;
    }

    public Map<Character, Integer> getFrequencies(String text) {
        return buildFrequencyMap(text);
    }

    // ===== Utility: Save Encoded Data to File =====
    public void saveEncodedFile(String encodedData, File outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(encodedData);
        }
    }

    // ===== Utility: Save Huffman Codes (for decompression use) =====
    public void saveCodeTable(File outputFile) throws IOException {
        if (huffmanCode == null) return;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (var entry : huffmanCode.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        }
    }

    // ===== Utility: Load Huffman Code Table from File =====
    public Map<Character, String> loadCodeTable(File inputFile) throws IOException {
        Map<Character, String> codeTable = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    codeTable.put(parts[0].charAt(0), parts[1]);
                }
            }
        }

        this.huffmanCode = codeTable;
        return codeTable;
    }
}
