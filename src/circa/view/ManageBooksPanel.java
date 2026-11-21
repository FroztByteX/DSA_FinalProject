package circa.view;

import circa.controller.AdminController;
import circa.controller.Result;
import circa.model.Book;
import circa.service.BookSort;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class ManageBooksPanel extends RefreshablePanel {
    private final AdminController controller;
    private final JTextField searchField = new JTextField();
    private final JComboBox<BookSort> sortBox = new JComboBox<>(BookSort.values());
    private final DefaultTableModel model =
            Widgets.readOnlyModel("Book ID", "Title", "Author", "Category", "Copies", "Status");
    private final JTable table = new JTable(model);
    private Book[] rows = new Book[0];

    public ManageBooksPanel(AdminController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton searchButton = new JButton("Search");
        JButton addButton = new JButton("+ Add Book");
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        controls.add(new JLabel("Sort by:"));
        controls.add(sortBox);
        controls.add(searchButton);
        controls.add(addButton);
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.add(searchField, BorderLayout.CENTER);
        toolbar.add(controls, BorderLayout.EAST);

        JPanel north = new JPanel(new BorderLayout(5, 10));
        north.add(new JLabel("Manage Books - Inventory"), BorderLayout.NORTH);
        north.add(toolbar, BorderLayout.CENTER);

        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(editButton);
        actions.add(deleteButton);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> refresh());
        searchField.addActionListener(e -> refresh());
        sortBox.addActionListener(e -> refresh());
        addButton.addActionListener(e -> addBook());
        editButton.addActionListener(e -> editBook());
        deleteButton.addActionListener(e -> deleteBook());
    }

    private Book selectedBook() {
        int row = table.getSelectedRow();
        if (row < 0) {
            Messages.info(this, "Select a book first.");
            return null;
        }
        return rows[row];
    }

    private void addBook() {
        BookFormDialog dialog = new BookFormDialog(SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            submit(dialog, true);
        }
    }

    private void editBook() {
        Book book = selectedBook();
        if (book == null) {
            return;
        }
        BookFormDialog dialog = new BookFormDialog(SwingUtilities.getWindowAncestor(this), book);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            submit(dialog, false);
        }
    }

    private void submit(BookFormDialog dialog, boolean isNew) {
        int copies;
        try {
            copies = Integer.parseInt(dialog.getCopiesText().trim());
        } catch (NumberFormatException e) {
            Messages.info(this, "Copies must be a whole number.");
            return;
        }
        Result<Void> result = isNew
                ? controller.addBook(dialog.getBookId(), dialog.getBookTitle(), dialog.getAuthor(),
                        dialog.getIsbn(), dialog.getCategory(), copies)
                : controller.updateBook(dialog.getBookId(), dialog.getBookTitle(), dialog.getAuthor(),
                        dialog.getIsbn(), dialog.getCategory(), copies);
        Messages.show(this, result);
        refresh();
    }

    private void deleteBook() {
        Book book = selectedBook();
        if (book == null || !Messages.confirm(this, "Delete \"" + book.getTitle() + "\"?")) {
            return;
        }
        Messages.show(this, controller.deleteBook(book.getId()));
        refresh();
    }

    @Override
    public void refresh() {
        rows = controller.searchInventory(searchField.getText(), (BookSort) sortBox.getSelectedItem());
        model.setRowCount(0);
        for (Book book : rows) {
            model.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor(), book.getCategory(),
                    book.getAvailableCopies() + "/" + book.getTotalCopies(), book.getStatus()});
        }
    }
}
