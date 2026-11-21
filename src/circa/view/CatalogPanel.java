package circa.view;

import circa.controller.Result;
import circa.controller.StudentController;
import circa.model.Book;
import circa.model.BorrowRecord;
import circa.model.Student;
import circa.service.BookSort;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CatalogPanel extends RefreshablePanel {
    private static final int PAGE_SIZE = 4;
    private static final String ALL_CATEGORIES = "All Categories";

    private final Student student;
    private final StudentController controller;
    private final JTextField searchField = new JTextField();
    private final JComboBox<String> categoryBox = new JComboBox<>();
    private final JComboBox<BookSort> sortBox = new JComboBox<>(BookSort.values());
    private final JPanel cardGrid = new JPanel(new GridLayout(1, PAGE_SIZE, 10, 10));
    private final JPanel pagination = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
    private Book[] results = new Book[0];
    private int page = 1;
    private boolean loading;

    public CatalogPanel(Student student, StudentController controller) {
        this.student = student;
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton searchButton = new JButton("Search");
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        controls.add(categoryBox);
        controls.add(sortBox);
        controls.add(searchButton);
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.add(searchField, BorderLayout.CENTER);
        toolbar.add(controls, BorderLayout.EAST);

        add(toolbar, BorderLayout.NORTH);
        add(cardGrid, BorderLayout.CENTER);
        add(pagination, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> runSearch(true));
        searchField.addActionListener(e -> runSearch(true));
        categoryBox.addActionListener(e -> {
            if (!loading) {
                runSearch(true);
            }
        });
        sortBox.addActionListener(e -> {
            if (!loading) {
                runSearch(true);
            }
        });
    }

    @Override
    public void refresh() {
        loading = true;
        Object selected = categoryBox.getSelectedItem();
        categoryBox.removeAllItems();
        categoryBox.addItem(ALL_CATEGORIES);
        for (String category : controller.categories()) {
            categoryBox.addItem(category);
        }
        categoryBox.setSelectedItem(selected == null ? ALL_CATEGORIES : selected);
        loading = false;
        runSearch(false);
    }

    private void runSearch(boolean resetPage) {
        String category = (String) categoryBox.getSelectedItem();
        results = controller.searchBooks(searchField.getText(),
                ALL_CATEGORIES.equals(category) ? null : category, (BookSort) sortBox.getSelectedItem());
        if (resetPage) {
            page = 1;
        }
        renderPage();
    }

    private int totalPages() {
        return Math.max(1, (results.length + PAGE_SIZE - 1) / PAGE_SIZE);
    }

    private void renderPage() {
        page = Math.min(Math.max(page, 1), totalPages());
        cardGrid.removeAll();
        int start = (page - 1) * PAGE_SIZE;
        for (int i = 0; i < PAGE_SIZE; i++) {
            int index = start + i;
            if (index < results.length) {
                Book book = results[index];
                cardGrid.add(new CatalogCardPanel(book, e -> showDetails(book), e -> borrow(book)));
            } else {
                cardGrid.add(new JPanel());
            }
        }
        cardGrid.revalidate();
        cardGrid.repaint();
        renderPagination();
    }

    private void renderPagination() {
        pagination.removeAll();
        JButton previous = new JButton("< Prev");
        previous.setEnabled(page > 1);
        previous.addActionListener(e -> goToPage(page - 1));
        pagination.add(previous);
        for (int p = 1; p <= totalPages(); p++) {
            int target = p;
            JButton number = new JButton(String.valueOf(p));
            number.setEnabled(p != page);
            number.addActionListener(e -> goToPage(target));
            pagination.add(number);
        }
        JButton next = new JButton("Next >");
        next.setEnabled(page < totalPages());
        next.addActionListener(e -> goToPage(page + 1));
        pagination.add(next);
        pagination.revalidate();
        pagination.repaint();
    }

    private void goToPage(int target) {
        page = target;
        renderPage();
    }

    private void showDetails(Book book) {
        Messages.info(this, "Book ID: " + book.getId()
                + "\nTitle: " + book.getTitle()
                + "\nAuthor: " + book.getAuthor()
                + "\nISBN: " + book.getIsbn()
                + "\nCategory: " + book.getCategory()
                + "\nCopies available: " + book.getAvailableCopies() + " of " + book.getTotalCopies()
                + "\nStudents in reservation queue: " + book.getReservations().size());
    }

    private boolean holdsBook(Book book) {
        for (BorrowRecord record : controller.activeLoans(student)) {
            if (record.getBook() == book) {
                return true;
            }
        }
        return false;
    }

    private void borrow(Book book) {
        Result<BorrowRecord> result = controller.borrow(student, book.getId());
        boolean canReserve = !result.isSuccess() && !book.isAvailable() && !holdsBook(book);
        if (!canReserve) {
            Messages.show(this, result);
        } else if (Messages.confirm(this, "No copies are available right now. Reserve this book instead?")) {
            Messages.show(this, controller.reserve(student, book.getId()));
        }
        runSearch(false);
    }
}
