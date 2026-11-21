package circa.storage;

import circa.model.Book;
import circa.model.BorrowRecord;
import circa.model.Librarian;
import circa.model.Person;
import circa.model.Student;
import circa.model.Transaction;
import circa.model.TransactionType;
import circa.model.UserRole;
import circa.repository.BookRepository;
import circa.repository.BorrowRecordRepository;
import circa.repository.TransactionRepository;
import circa.repository.UserRepository;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class FileDataStore implements DataStore {
    private static final String USERS = "users.txt";
    private static final String BOOKS = "books.txt";
    private static final String RECORDS = "borrow_records.txt";
    private static final String TRANSACTIONS = "transactions.txt";

    private final Path directory;
    private final UserRepository users;
    private final BookRepository books;
    private final BorrowRecordRepository records;
    private final TransactionRepository transactions;

    public FileDataStore(Path directory, UserRepository users, BookRepository books,
                         BorrowRecordRepository records, TransactionRepository transactions) {
        this.directory = directory;
        this.users = users;
        this.books = books;
        this.records = records;
        this.transactions = transactions;
    }

    @Override
    public boolean hasSavedData() {
        return Files.exists(directory.resolve(USERS)) && Files.exists(directory.resolve(BOOKS))
                && Files.exists(directory.resolve(RECORDS)) && Files.exists(directory.resolve(TRANSACTIONS));
    }

    @Override
    public synchronized void save() {
        try {
            Files.createDirectories(directory);
            writeFile(USERS, usersText());
            writeFile(BOOKS, booksText());
            writeFile(RECORDS, recordsText());
            writeFile(TRANSACTIONS, transactionsText());
        } catch (IOException e) {
            throw new StorageException("Your change was applied but could not be saved to disk: " + e.getMessage(), e);
        }
    }

    private void writeFile(String name, String content) throws IOException {
        Path temp = directory.resolve(name + ".tmp");
        Files.writeString(temp, content, StandardCharsets.UTF_8);
        Files.move(temp, directory.resolve(name), StandardCopyOption.REPLACE_EXISTING);
    }

    private String usersText() {
        StringBuilder text = new StringBuilder(TextRecord.join("role", "id", "name", "passwordHash")).append('\n');
        for (Person person : users.findAll()) {
            text.append(TextRecord.join(person.getRole().name(), person.getId(), person.getName(),
                    person.getPasswordHash())).append('\n');
        }
        return text.toString();
    }

    private String booksText() {
        StringBuilder text = new StringBuilder(TextRecord.join("id", "title", "author", "isbn", "category",
                "totalCopies", "availableCopies", "reservations")).append('\n');
        for (Book book : books.findAll()) {
            StringBuilder queue = new StringBuilder();
            for (String studentId : book.getReservations()) {
                if (queue.length() > 0) {
                    queue.append(';');
                }
                queue.append(studentId);
            }
            text.append(TextRecord.join(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(),
                    book.getCategory(), String.valueOf(book.getTotalCopies()),
                    String.valueOf(book.getAvailableCopies()), queue.toString())).append('\n');
        }
        return text.toString();
    }

    private String recordsText() {
        StringBuilder text = new StringBuilder(TextRecord.join("id", "bookId", "studentId", "borrowDate",
                "dueDate", "returnDate")).append('\n');
        for (BorrowRecord record : records.findAll()) {
            text.append(TextRecord.join(String.valueOf(record.getId()), record.getBook().getId(),
                    record.getStudent().getId(), record.getBorrowDate().toString(),
                    record.getDueDate().toString(),
                    record.getReturnDate() == null ? "" : record.getReturnDate().toString())).append('\n');
        }
        return text.toString();
    }

    private String transactionsText() {
        StringBuilder text = new StringBuilder(TextRecord.join("id", "type", "bookId", "studentId", "date")).append('\n');
        for (Transaction t : transactions.findAll()) {
            text.append(TextRecord.join(String.valueOf(t.getId()), t.getType().name(), t.getBook().getId(),
                    t.getStudent().getId(), t.getDate().toString())).append('\n');
        }
        return text.toString();
    }

    @Override
    public synchronized void load() {
        try {
            loadUsers();
            loadBooks();
            loadRecords();
            loadTransactions();
        } catch (StorageException e) {
            throw e;
        } catch (IOException | RuntimeException e) {
            throw new StorageException("Saved data is unreadable: " + e.getMessage(), e);
        }
    }

    private String[][] readRows(String name) throws IOException {
        java.util.List<String> lines = Files.readAllLines(directory.resolve(name), StandardCharsets.UTF_8);
        int count = 0;
        String[][] rows = new String[Math.max(0, lines.size() - 1)][];
        for (int i = 1; i < lines.size(); i++) {
            if (!lines.get(i).isBlank()) {
                rows[count++] = TextRecord.split(lines.get(i));
            }
        }
        return java.util.Arrays.copyOf(rows, count);
    }

    private void loadUsers() throws IOException {
        for (String[] f : readRows(USERS)) {
            UserRole role = UserRole.valueOf(f[0]);
            Person person = role == UserRole.LIBRARIAN
                    ? new Librarian(f[1], f[2], f[3], true)
                    : new Student(f[1], f[2], f[3], true);
            users.save(person);
        }
    }

    private void loadBooks() throws IOException {
        for (String[] f : readRows(BOOKS)) {
            Book book = new Book(f[0], f[1], f[2], f[3], f[4], Integer.parseInt(f[5]));
            book.restoreAvailableCopies(Integer.parseInt(f[6]));
            if (f.length > 7 && !f[7].isBlank()) {
                for (String studentId : f[7].split(";")) {
                    book.getReservations().enqueue(studentId);
                }
            }
            books.save(book);
        }
    }

    private void loadRecords() throws IOException {
        for (String[] f : readRows(RECORDS)) {
            Book book = books.findById(f[1]);
            Student student = users.findStudent(f[2]);
            if (book == null || student == null) {
                throw new StorageException("Borrow record " + f[0] + " refers to a missing book or student.");
            }
            BorrowRecord record = new BorrowRecord(Integer.parseInt(f[0]), book, student,
                    LocalDate.parse(f[3]), LocalDate.parse(f[4]));
            if (f.length > 5 && !f[5].isBlank()) {
                record.markReturned(LocalDate.parse(f[5]));
            }
            records.save(record);
        }
    }

    private void loadTransactions() throws IOException {
        for (String[] f : readRows(TRANSACTIONS)) {
            Book book = books.findById(f[2]);
            Student student = users.findStudent(f[3]);
            if (book == null || student == null) {
                throw new StorageException("Transaction " + f[0] + " refers to a missing book or student.");
            }
            Transaction transaction = new Transaction(Integer.parseInt(f[0]), TransactionType.valueOf(f[1]),
                    book, student, LocalDate.parse(f[4]));
            transactions.save(transaction);
            student.getHistory().addLast(transaction);
        }
    }
}
