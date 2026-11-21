package circa.service;

import circa.model.Book;
import java.util.Comparator;

public enum BookSort {
    TITLE("Title", Comparator.comparing((Book b) -> b.getTitle().toLowerCase())),
    AUTHOR("Author", Comparator.comparing((Book b) -> b.getAuthor().toLowerCase())),
    CATEGORY("Category", Comparator.comparing((Book b) -> b.getCategory().toLowerCase())),
    ID("Book ID", Comparator.comparing((Book b) -> b.getId()));

    private final String label;
    private final Comparator<Book> comparator;

    BookSort(String label, Comparator<Book> comparator) {
        this.label = label;
        this.comparator = comparator;
    }

    public Comparator<Book> comparator() {
        return comparator;
    }

    @Override
    public String toString() {
        return label;
    }
}
