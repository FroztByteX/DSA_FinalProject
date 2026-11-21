package circa.view;

import circa.controller.StudentController;
import circa.model.Student;
import circa.model.Transaction;
import circa.service.TransactionSort;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TransactionHistoryPanel extends RefreshablePanel {
    private final Student student;
    private final StudentController controller;
    private final JComboBox<TransactionSort> sortBox = new JComboBox<>(TransactionSort.values());
    private final DefaultTableModel model = Widgets.readOnlyModel("Transaction ID", "Type", "Book", "Date");
    private final JLabel cursorLabel = new JLabel();

    public TransactionHistoryPanel(Student student, StudentController controller) {
        this.student = student;
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Sort by:"));
        top.add(sortBox);

        JButton previous = new JButton("< Previous");
        JButton next = new JButton("Next >");
        JPanel navigation = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navigation.setBorder(BorderFactory.createTitledBorder("Browse Records"));
        navigation.add(previous);
        navigation.add(next);
        navigation.add(cursorLabel);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
        add(navigation, BorderLayout.SOUTH);

        sortBox.addActionListener(e -> loadTable());
        previous.addActionListener(e -> {
            controller.moveHistoryPrevious(student);
            showCursor();
        });
        next.addActionListener(e -> {
            controller.moveHistoryNext(student);
            showCursor();
        });
    }

    @Override
    public void refresh() {
        controller.resetHistoryCursor(student);
        loadTable();
        showCursor();
    }

    private void loadTable() {
        model.setRowCount(0);
        for (Transaction t : controller.transactions(student, (TransactionSort) sortBox.getSelectedItem())) {
            model.addRow(new Object[]{t.getId(), t.getType(), t.getBook().getTitle(), Formats.date(t.getDate())});
        }
    }

    private void showCursor() {
        Transaction current = controller.currentTransaction(student);
        if (current == null) {
            cursorLabel.setText("No transactions yet.");
            return;
        }
        cursorLabel.setText("Record " + controller.historyPosition(student) + " of " + controller.historySize(student)
                + ": " + current.getType() + " - " + current.getBook().getTitle() + " on "
                + Formats.date(current.getDate()));
    }
}
