package ui;
import java.awt.*;

/**
 * Centralized UI Theme for consistent styling across all panels.
 * You can import this class anywhere in the ui package.
 */
public class Theme {
    // ===== Color Palette =====
    public static final Color PRIMARY_COLOR = new Color(33, 150, 243);     // Blue header
    public static final Color SECONDARY_COLOR = new Color(245, 247, 250);  // Background
    public static final Color BUTTON_COLOR = new Color(52, 152, 219);      // Button blue
    public static final Color BUTTON_HOVER = new Color(41, 128, 185);      // Button hover
    public static final Color ACCENT_COLOR = new Color(76, 175, 80);       // Success green
    public static final Color TEXT_COLOR = Color.WHITE;                    // Text on blue
    public static final Color FOOTER_COLOR = new Color(100, 100, 100);     // Footer gray

    // ===== Fonts =====
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FOOTER_FONT = new Font("Segoe UI", Font.ITALIC, 14);

    // ===== Shadows & Borders =====
    public static final BorderLayout DEFAULT_BORDER = new BorderLayout();
}
