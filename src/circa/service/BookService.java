package circa.service;

import circa.model.Book;
import circa.repository.BookRepository;
import circa.storage.DataStore;
import circa.structures.MergeSort;
import circa.structures.MyHashTable;
import circa.structures.MyLinkedList;

public class BookService {
    private final BookRepository books;
    private final DataStore store;

    public BookService(BookRepository books, DataStore store) {
        this.books = books;
        this.store = store;
    }

    public Book findById(String id) {
        return books.findById(id);
    }

    public Book[] search(String keyword, String category, BookSort sort) {
        String key = keyword == null ? "" : keyword.trim().toLowerCase();
        boolean anyCategory = category == null || category.isBlank();
        MyLinkedList<Book> matches = new MyLinkedList<>();
        if (!key.isEmpty()) {
            Book exact = books.findById(key);
            if (exact != null && (anyCategory || exact.getCategory().equalsIgnoreCase(category))) {
                matches.add(exact);
            }
        }
        for (Book book : books.findAllByTitle()) {
            if (!anyCategory && !book.getCategory().equalsIgnoreCase(category)) {
                continue;
            }
            if (matchesKeyword(book, key) && !contains(matches, book)) {
                matches.add(book);
            }
        }
        Book[] result = matches.toArray(new Book[0]);
        if (sort != null && sort != BookSort.TITLE) {
            MergeSort.sort(result, sort.comparator());
        } else {
            MergeSort.sort(result, BookSort.TITLE.comparator());
        }
        return result;
    }

    private boolean contains(MyLinkedList<Book> list, Book book) {
        for (Book b : list) {
            if (b == book) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesKeyword(Book book, String key) {
        return key.isEmpty()
                || book.getId().toLowerCase().contains(key)
                || book.getTitle().toLowerCase().contains(key)
                || book.getAuthor().toLowerCase().contains(key)
                || book.getIsbn().toLowerCase().contains(key)
                || book.getCategory().toLowerCase().contains(key);
    }

    public String[] categories() {
        MyHashTable<String, String> seen = new MyHashTable<>();
        MyLinkedList<String> names = new MyLinkedList<>();
        for (Book book : books.findAll()) {
            String key = book.getCategory().toLowerCase();
            if (!seen.containsKey(key)) {
                seen.put(key, book.getCategory());
                names.add(book.getCategory());
            }
        }
        String[] result = names.toArray(new String[0]);
        MergeSort.sort(result, String.CASE_INSENSITIVE_ORDER);
        return result;
    }

    public Book[] recentlyAdded(int count) {
        return books.findMostRecent(count);
    }

    public int countAvailableCopies() {
        int total = 0;
        for (Book book : books.findAll()) {
            total += book.getAvailableCopies();
        }
        return total;
    }

    public void addBook(String id, String title, String author, String isbn, String category, int copies)
            throws LibraryException {
        validate(id, title, author, category, copies);
        if (books.findById(id) != null) {
            throw new LibraryException("A book with ID " + id.trim().toUpperCase() + " already exists.");
        }
        books.save(new Book(id, title, author, isbn, category, copies));
        store.save();
    }

    public void updateBook(String id, String title, String author, String isbn, String category, int copies)
            throws LibraryException {
        validate(id, title, author, category, copies);
        Book book = books.findById(id);
        if (book == null) {
            throw new LibraryException("Book not found.");
        }
        try {
            book.updateTotalCopies(copies);
        } catch (IllegalArgumentException e) {
            throw new LibraryException(e.getMessage());
        }
        book.updateDetails(title, author, isbn, category);
        books.reindex();
        store.save();
    }

    public void deleteBook(String id) throws LibraryException {
        Book book = books.findById(id);
        if (book == null) {
            throw new LibraryException("Book not found.");
        }
        if (book.getBorrowedCopies() > 0) {
            throw new LibraryException("Cannot delete a book that still has borrowed copies.");
        }
        books.deleteById(id);
        store.save();
    }

    private void validate(String id, String title, String author, String category, int copies)
            throws LibraryException {
        if (id == null || id.isBlank() || title == null || title.isBlank()
                || author == null || author.isBlank() || category == null || category.isBlank()) {
            throw new LibraryException("Book ID, title, author and category are required.");
        }
        if (copies < 1) {
            throw new LibraryException("Copies must be at least 1.");
        }
    }
}
