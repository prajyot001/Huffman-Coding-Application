package Main;
import ui.*;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppContainer::new);
    }
}
