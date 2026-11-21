package circa.view;

import circa.controller.AdminController;
import circa.model.BorrowRecord;
import circa.service.RecordSort;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ManageBorrowingRecordsPanel extends RefreshablePanel {
    private final AdminController controller;
    private final JComboBox<RecordSort> sortBox = new JComboBox<>(RecordSort.values());
    private final DefaultTableModel model = Widgets.readOnlyModel(
            "Record ID", "Book", "Student", "Borrow Date", "Due Date", "Return Date", "Status");
    private final JList<String> actionList = new JList<>();

    public ManageBorrowingRecordsPanel(AdminController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton issueButton = new JButton("Issue Book");
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Manage Borrowing Records"));
        top.add(new JLabel("Sort by:"));
        top.add(sortBox);
        top.add(issueButton);

        JButton undoButton = new JButton("Undo Last Action");
        JPanel recent = new JPanel(new BorderLayout(5, 5));
        recent.setBorder(BorderFactory.createTitledBorder("Recent Actions (latest first)"));
        JScrollPane listScroll = new JScrollPane(actionList);
        listScroll.setPreferredSize(new Dimension(0, 110));
        recent.add(listScroll, BorderLayout.CENTER);
        recent.add(undoButton, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
        add(recent, BorderLayout.SOUTH);

        sortBox.addActionListener(e -> refresh());
        issueButton.addActionListener(e -> issueBook());
        undoButton.addActionListener(e -> {
            Messages.show(this, controller.undoLast());
            refresh();
        });
    }

    private void issueBook() {
        JTextField studentField = new JTextField();
        JTextField bookField = new JTextField();
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Student ID:"));
        form.add(studentField);
        form.add(new JLabel("Book ID:"));
        form.add(bookField);
        int choice = JOptionPane.showConfirmDialog(this, form, "Issue Book", JOptionPane.OK_CANCEL_OPTION);
        if (choice == JOptionPane.OK_OPTION) {
            Messages.show(this, controller.issueBook(studentField.getText(), bookField.getText()));
            refresh();
        }
    }

    @Override
    public void refresh() {
        LocalDate today = LocalDate.now();
        model.setRowCount(0);
        for (BorrowRecord record : controller.allRecords((RecordSort) sortBox.getSelectedItem())) {
            model.addRow(new Object[]{record.getId(), record.getBook().getTitle(), record.getStudent().getId(),
                    Formats.date(record.getBorrowDate()), Formats.date(record.getDueDate()),
                    Formats.date(record.getReturnDate()), record.getStatus(today)});
        }
        actionList.setListData(controller.recentActions());
    }
}
