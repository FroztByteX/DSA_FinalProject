package circa.view;

import circa.controller.AdminController;
import circa.model.Librarian;
import circa.structures.MyHashTable;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class AdminFrame extends BaseFrame implements Navigator {
    public static final String BOOKS = "Manage Books";
    public static final String STUDENTS = "Manage Students / Users";
    public static final String RECORDS = "Manage Borrowing Records";
    public static final String RETURNS = "Manage Returns";

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final MyHashTable<String, RefreshablePanel> screens = new MyHashTable<>();

    public AdminFrame(Librarian librarian, AdminController controller, Runnable onLogout) {
        super("CIRCA: LIBRARY RESERVATION AND BORROWING SYSTEM - ADMIN", librarian, 1150, 680, onLogout);

        register(BOOKS, new ManageBooksPanel(controller));
        register(STUDENTS, new ManageStudentsPanel(controller));
        register(RECORDS, new ManageBorrowingRecordsPanel(controller));
        register(RETURNS, new ManageReturnsPanel(controller));

        JPanel menu = new JPanel(new GridLayout(0, 1, 5, 5));
        String[] names = {BOOKS, STUDENTS, RECORDS, RETURNS};
        for (String name : names) {
            JButton button = new JButton(name);
            button.addActionListener(e -> navigateTo(name));
            menu.add(button);
        }
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> logout());

        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        sidebar.add(menu, BorderLayout.NORTH);
        sidebar.add(logout, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
        navigateTo(BOOKS);
    }

    private void register(String name, RefreshablePanel panel) {
        screens.put(name, panel);
        content.add(panel, name);
    }

    @Override
    public void navigateTo(String screenName) {
        RefreshablePanel panel = screens.get(screenName);
        if (panel == null) {
            return;
        }
        panel.refresh();
        cards.show(content, screenName);
    }
}
