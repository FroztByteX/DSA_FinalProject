package circa.view;

import circa.controller.StudentController;
import circa.model.BorrowRecord;
import circa.model.Student;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MyBorrowedPanel extends RefreshablePanel {
    private final Student student;
    private final StudentController controller;
    private final DefaultTableModel borrowedModel =
            Widgets.readOnlyModel("Book Title", "Borrow Date", "Due Date", "Return Status");
    private final DefaultTableModel historyModel =
            Widgets.readOnlyModel("Book Title", "Borrow Date", "Return Date", "Status", "Details");
    private final JTable borrowedTable = new JTable(borrowedModel);
    private BorrowRecord[] borrowedRows = new BorrowRecord[0];

    public MyBorrowedPanel(Student student, StudentController controller) {
        this.student = student;
        this.controller = controller;
        setLayout(new GridLayout(2, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton returnButton = new JButton("Return Book");
        returnButton.addActionListener(e -> returnSelected());
        JPanel borrowed = new JPanel(new BorderLayout());
        borrowed.setBorder(BorderFactory.createTitledBorder("My Borrowed Books"));
        borrowed.add(new JScrollPane(borrowedTable), BorderLayout.CENTER);
        borrowed.add(returnButton, BorderLayout.SOUTH);

        JPanel history = new JPanel(new BorderLayout());
        history.setBorder(BorderFactory.createTitledBorder("Borrowing History"));
        history.add(new JScrollPane(new JTable(historyModel)), BorderLayout.CENTER);

        add(borrowed);
        add(history);
    }

    private void returnSelected() {
        int row = borrowedTable.getSelectedRow();
        if (row < 0) {
            Messages.info(this, "Select a borrowed book first.");
            return;
        }
        Messages.show(this, controller.returnBook(borrowedRows[row].getId()));
        refresh();
    }

    @Override
    public void refresh() {
        LocalDate today = LocalDate.now();
        borrowedRows = controller.activeLoans(student);
        borrowedModel.setRowCount(0);
        for (BorrowRecord record : borrowedRows) {
            borrowedModel.addRow(new Object[]{record.getBook().getTitle(), Formats.date(record.getBorrowDate()),
                    Formats.date(record.getDueDate()), record.getStatus(today)});
        }
        historyModel.setRowCount(0);
        for (BorrowRecord record : controller.returnedHistory(student)) {
            historyModel.addRow(new Object[]{record.getBook().getTitle(), Formats.date(record.getBorrowDate()),
                    Formats.date(record.getReturnDate()), record.getStatus(today), record.getDetails()});
        }
    }
}
