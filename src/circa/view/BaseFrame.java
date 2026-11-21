package circa.view;

import circa.model.Person;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class BaseFrame extends JFrame {
    private final JPanel northPanel = new JPanel(new BorderLayout());
    private final Runnable onLogout;

    protected BaseFrame(String systemTitle, Person user, int width, int height, Runnable onLogout) {
        super("CIRCA");
        this.onLogout = onLogout;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);

        JPanel header = new JPanel(new BorderLayout(10, 0));
<<<<<<< HEAD
        header.add(Widgets.logo(56, 56), BorderLayout.WEST);
=======
        header.add(Widgets.imagePlaceholder(48, 48), BorderLayout.WEST);
>>>>>>> 1a93c7cd73cd4ada2f0ab6542ebd5310c69289ca
        header.add(new JLabel(systemTitle), BorderLayout.CENTER);
        header.add(new JLabel("Hello, " + user.getName()), BorderLayout.EAST);
        northPanel.add(header, BorderLayout.NORTH);
        add(northPanel, BorderLayout.NORTH);
    }

    protected JPanel getNorthPanel() {
        return northPanel;
    }

    protected void logout() {
        dispose();
        onLogout.run();
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 1a93c7cd73cd4ada2f0ab6542ebd5310c69289ca
