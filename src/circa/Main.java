package circa;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, error) -> SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null,
                        error.getMessage() == null ? error.toString() : error.getMessage(),
                        "CIRCA", JOptionPane.ERROR_MESSAGE)));
        SwingUtilities.invokeLater(() -> new AppContext().start());
    }
}
