package circa.view;

import circa.controller.StudentController;
import circa.model.Book;
import circa.model.Student;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class BorrowBooksPanel extends RefreshablePanel {
    private final Student student;
    private final StudentController controller;
    private final JTextField bookIdField = new JTextField(12);
    private final DefaultTableModel model = Widgets.readOnlyModel("Book ID", "Title", "Queue Position");
    private final JTable table = new JTable(model);
    private Book[] rows = new Book[0];

    public BorrowBooksPanel(Student student, StudentController controller) {
        this.student = student;
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton borrow = new JButton("Borrow");
        JButton reserve = new JButton("Reserve");
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBorder(BorderFactory.createTitledBorder("Borrow or Reserve a Book"));
        top.add(new JLabel("Book ID:"));
        top.add(bookIdField);
        top.add(borrow);
        top.add(reserve);

        JButton cancel = new JButton("Cancel Selected Request");
        JPanel pending = new JPanel(new BorderLayout());
        pending.setBorder(BorderFactory.createTitledBorder("My Pending Borrow Requests"));
        pending.add(new JScrollPane(table), BorderLayout.CENTER);
        pending.add(cancel, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(pending, BorderLayout.CENTER);

        borrow.addActionListener(e -> {
            Messages.show(this, controller.borrow(student, bookIdField.getText()));
            refresh();
        });
        reserve.addActionListener(e -> {
            Messages.show(this, controller.reserve(student, bookIdField.getText()));
            refresh();
        });
        cancel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                Messages.info(this, "Select a request first.");
                return;
            }
            Messages.show(this, controller.cancelReservation(student, rows[row].getId()));
            refresh();
        });
    }

    @Override
    public void refresh() {
        rows = controller.pendingReservations(student);
        model.setRowCount(0);
        for (Book book : rows) {
            model.addRow(new Object[]{book.getId(), book.getTitle(), controller.queuePosition(student, book)});
        }
    }
}
