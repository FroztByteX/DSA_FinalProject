package circa.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public final class Widgets {
    private static final String[] LOGO_FOLDERS = {"images", "../images", "CIRCA/images"};
    private static final String[] LOGO_NAMES = {"logo.png", "logo.jpg", "logo.jpeg"};
    private static boolean warned;

    private Widgets() {
    }

    private static ImageIcon loadLogo() {
        for (String folder : LOGO_FOLDERS) {
            for (String name : LOGO_NAMES) {
                Path file = Paths.get(folder, name);
                if (Files.isRegularFile(file)) {
                    ImageIcon icon = new ImageIcon(file.toString());
                    if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                        return icon;
                    }
                }
            }
        }
        for (String name : LOGO_NAMES) {
            java.net.URL url = Widgets.class.getResource("/images/" + name);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                    return icon;
                }
            }
        }
        return null;
    }

    public static JLabel logo(int width, int height) {
        ImageIcon icon = loadLogo();
        if (icon == null) {
            String expected = Paths.get("images", "logo.png").toAbsolutePath().toString();
            if (!warned) {
                warned = true;
                System.err.println("CIRCA logo not found. Save it as: " + expected);
            }
            JLabel placeholder = imagePlaceholder(width, height);
            placeholder.setText("[ logo ]");
            placeholder.setToolTipText("Save your logo as: " + expected);
            return placeholder;
        }
        double scale = Math.min((double) width / icon.getIconWidth(), (double) height / icon.getIconHeight());
        int scaledWidth = Math.max(1, (int) (icon.getIconWidth() * scale));
        int scaledHeight = Math.max(1, (int) (icon.getIconHeight() * scale));
        Image scaled = icon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
        fixSize(label, width, height);
        return label;
    }

    private static void fixSize(JLabel label, int width, int height) {
        Dimension size = new Dimension(width, height);
        label.setPreferredSize(size);
        label.setMinimumSize(size);
        label.setMaximumSize(size);
    }

    public static JLabel imagePlaceholder(int width, int height) {
        JLabel label = new JLabel("[ image ]", SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        fixSize(label, width, height);
        return label;
    }

    public static DefaultTableModel readOnlyModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}