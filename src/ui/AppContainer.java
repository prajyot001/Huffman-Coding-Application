package ui;

import javax.swing.*;
import java.awt.*;

public class AppContainer extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public AppContainer() {
        setTitle(" Huffman Coding Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.SECONDARY_COLOR);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Theme.SECONDARY_COLOR);

        // Add panels
        mainPanel.add(new HomePanel(this), "home");
        mainPanel.add(new TextCompressionPanel(this), "textCompress");
        mainPanel.add(new TextDecompressionPanel(this), "textDecompress");
        mainPanel.add(new FileCompressionPanel(this), "fileCompress");
        mainPanel.add(new FileDecompressionPanel(this), "fileDecompress");

        add(mainPanel);
        setVisible(true);
    }

    // ===== Navigation Methods =====
    public void showHomePanel() { cardLayout.show(mainPanel, "home"); }
    public void showTextCompressionPanel() { cardLayout.show(mainPanel, "textCompress"); }
    public void showTextDecompressionPanel() { cardLayout.show(mainPanel, "textDecompress"); }
    public void showFileCompressionPanel() { cardLayout.show(mainPanel, "fileCompress"); }
    public void showFileDecompressionPanel() { cardLayout.show(mainPanel, "fileDecompress"); }
}
