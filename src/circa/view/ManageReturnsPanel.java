package circa.view;

import circa.controller.AdminController;
import circa.model.BorrowRecord;
import circa.service.RecordSort;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ManageReturnsPanel extends RefreshablePanel {
    private final AdminController controller;
    private final JTextField searchField = new JTextField();
    private final DefaultTableModel model = Widgets.readOnlyModel(
            "Record ID", "Book", "Student", "Borrow Date", "Due Date", "Status");
    private final JTable table = new JTable(model);
    private BorrowRecord[] rows = new BorrowRecord[0];

    public ManageReturnsPanel(AdminController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton searchButton = new JButton("Search");
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.add(searchField, BorderLayout.CENTER);
        toolbar.add(searchButton, BorderLayout.EAST);
        JPanel north = new JPanel(new BorderLayout(5, 10));
        north.add(new JLabel("Manage Returns (search by student ID, book ID or title)"), BorderLayout.NORTH);
        north.add(toolbar, BorderLayout.CENTER);

        JButton returnButton = new JButton("Process Return");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(returnButton);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> refresh());
        searchField.addActionListener(e -> refresh());
        returnButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                Messages.info(this, "Select a borrowed book first.");
                return;
            }
            Messages.show(this, controller.processReturn(rows[row].getId()));
            refresh();
        });
    }

    private boolean matches(BorrowRecord record, String key) {
        return key.isEmpty()
                || record.getStudent().getId().toLowerCase().contains(key)
                || record.getBook().getId().toLowerCase().contains(key)
                || record.getBook().getTitle().toLowerCase().contains(key);
    }

    @Override
    public void refresh() {
        String key = searchField.getText().trim().toLowerCase();
        LocalDate today = LocalDate.now();
        BorrowRecord[] all = controller.activeRecords(RecordSort.OLDEST);
        int count = 0;
        BorrowRecord[] filtered = new BorrowRecord[all.length];
        for (BorrowRecord record : all) {
            if (matches(record, key)) {
                filtered[count++] = record;
            }
        }
        rows = java.util.Arrays.copyOf(filtered, count);
        model.setRowCount(0);
        for (BorrowRecord record : rows) {
            model.addRow(new Object[]{record.getId(), record.getBook().getTitle(), record.getStudent().getId(),
                    Formats.date(record.getBorrowDate()), Formats.date(record.getDueDate()), record.getStatus(today)});
        }
    }
}
