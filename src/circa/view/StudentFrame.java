package circa.view;

import circa.controller.StudentController;
import circa.model.Student;
import circa.structures.MyHashTable;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class StudentFrame extends BaseFrame implements Navigator {
    public static final String DASHBOARD = "Dashboard";
    public static final String CATALOG = "Book Catalog";
    public static final String BORROW = "Borrow Books";
    public static final String MY_BORROWED = "My Borrowed Books";
    public static final String HISTORY = "Transaction History";

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final MyHashTable<String, RefreshablePanel> screens = new MyHashTable<>();

    public StudentFrame(Student student, StudentController controller, Runnable onLogout) {
        super("CIRCA: LIBRARY RESERVATION AND BORROWING SYSTEM", student, 1050, 650, onLogout);

        register(DASHBOARD, new DashboardPanel(student, controller, this));
        register(CATALOG, new CatalogPanel(student, controller));
        register(BORROW, new BorrowBooksPanel(student, controller));
        register(MY_BORROWED, new MyBorrowedPanel(student, controller));
        register(HISTORY, new TransactionHistoryPanel(student, controller));

        JPanel navLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] names = {DASHBOARD, CATALOG, BORROW, MY_BORROWED, HISTORY};
        for (String name : names) {
            JButton button = new JButton(name);
            button.addActionListener(e -> navigateTo(name));
            navLeft.add(button);
        }
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> logout());
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.add(navLeft, BorderLayout.WEST);
        navBar.add(logout, BorderLayout.EAST);

        getNorthPanel().add(navBar, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);
        navigateTo(DASHBOARD);
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
