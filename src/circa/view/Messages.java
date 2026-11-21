package circa.view;

import circa.controller.Result;
import java.awt.Component;
import javax.swing.JOptionPane;

public final class Messages {
    private static final String TITLE = "CIRCA";

    private Messages() {
    }

    public static void show(Component parent, Result<?> result) {
        JOptionPane.showMessageDialog(parent, result.getMessage(), TITLE,
                result.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, TITLE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
