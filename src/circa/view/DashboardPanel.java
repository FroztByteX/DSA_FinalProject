package circa.view;

import circa.controller.StudentController;
import circa.model.Book;
import circa.model.Student;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DashboardPanel extends RefreshablePanel {
    private static final int RECENT_COUNT = 4;

    private final Student student;
    private final StudentController controller;
    private final Navigator navigator;
    private final JLabel welcomeLabel = new JLabel();
    private final JLabel availableLabel = new JLabel("0", SwingConstants.CENTER);
    private final JLabel borrowedLabel = new JLabel("0", SwingConstants.CENTER);
    private final JLabel overdueLabel = new JLabel("0", SwingConstants.CENTER);
    private final JLabel pendingLabel = new JLabel("0", SwingConstants.CENTER);
    private final JPanel recentGrid = new JPanel(new GridLayout(1, RECENT_COUNT, 10, 10));

    public DashboardPanel(Student student, StudentController controller, Navigator navigator) {
        this.student = student;
        this.controller = controller;
        this.navigator = navigator;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel stats = new JPanel(new GridLayout(1, 4, 10, 10));
        stats.add(statCard("Total Available Books", availableLabel));
        stats.add(statCard("Currently Borrowed Books", borrowedLabel));
        stats.add(statCard("Overdue Books", overdueLabel));
        stats.add(statCard("Pending Borrow Requests", pendingLabel));

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.add(welcomeLabel, BorderLayout.NORTH);
        top.add(stats, BorderLayout.CENTER);

        JPanel recent = new JPanel(new BorderLayout());
        recent.setBorder(BorderFactory.createTitledBorder("Recently Added Books"));
        recent.add(recentGrid, BorderLayout.CENTER);

        JPanel quick = new JPanel(new GridLayout(0, 1, 10, 10));
        quick.setBorder(BorderFactory.createTitledBorder("Quick Actions"));
        JButton search = new JButton("Search Books");
        JButton borrow = new JButton("Borrow Books");
        search.addActionListener(e -> navigator.navigateTo(StudentFrame.CATALOG));
        borrow.addActionListener(e -> navigator.navigateTo(StudentFrame.BORROW));
        quick.add(search);
        quick.add(borrow);
        quick.add(new JLabel());
        quick.add(new JLabel());
        JPanel quickWrapper = new JPanel(new BorderLayout());
        quickWrapper.add(quick, BorderLayout.CENTER);
        quickWrapper.setPreferredSize(new Dimension(220, 0));

        add(top, BorderLayout.NORTH);
        add(recent, BorderLayout.CENTER);
        add(quickWrapper, BorderLayout.EAST);
    }

    private JPanel statCard(String caption, JLabel value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createTitledBorder(caption));
        card.add(value, BorderLayout.CENTER);
        return card;
    }

    @Override
    public void refresh() {
        welcomeLabel.setText("Welcome back, " + student.getName() + "!");
        availableLabel.setText(String.valueOf(controller.totalAvailableCopies()));
        borrowedLabel.setText(String.valueOf(controller.activeLoanCount(student)));
        overdueLabel.setText(String.valueOf(controller.overdueCount(student)));
        pendingLabel.setText(String.valueOf(controller.pendingRequestCount(student)));

        recentGrid.removeAll();
        Book[] recent = controller.recentBooks(RECENT_COUNT);
        for (int i = 0; i < RECENT_COUNT; i++) {
            recentGrid.add(i < recent.length ? new BookCardPanel(recent[i]) : new JPanel());
        }
        recentGrid.revalidate();
        recentGrid.repaint();
    }
}
