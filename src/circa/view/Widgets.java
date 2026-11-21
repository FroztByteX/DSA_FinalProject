package circa.view;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public final class Widgets {
    private Widgets() {
    }

    public static JLabel imagePlaceholder(int width, int height) {
        JLabel label = new JLabel("[ image ]", SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setPreferredSize(new Dimension(width, height));
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
