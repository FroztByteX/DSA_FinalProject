package circa.service;

import circa.model.Book;
import circa.model.BorrowRecord;
import circa.model.Student;
import circa.model.Transaction;
import circa.model.TransactionType;
import circa.repository.BookRepository;
import circa.repository.BorrowRecordRepository;
import circa.repository.TransactionRepository;
import circa.repository.UserRepository;
import circa.storage.DataStore;
import circa.structures.MergeSort;
import circa.structures.MyLinkedList;
import circa.undo.BorrowAction;
import circa.undo.ReserveAction;
import circa.undo.ReturnAction;
import circa.undo.UndoManager;
import circa.undo.UndoableAction;
import java.time.LocalDate;

public class BorrowingService {
    public static final int LOAN_DAYS = 7;
    public static final int MAX_ACTIVE_LOANS = 5;

    private final BookRepository books;
    private final UserRepository users;
    private final BorrowRecordRepository records;
    private final TransactionRepository transactions;
    private final UndoManager undoManager;
    private final DataStore store;

    public BorrowingService(BookRepository books, UserRepository users, BorrowRecordRepository records,
                            TransactionRepository transactions, UndoManager undoManager, DataStore store) {
        this.store = store;
        this.books = books;
        this.users = users;
        this.records = records;
        this.transactions = transactions;
        this.undoManager = undoManager;
    }

    private Student requireStudent(String studentId) throws LibraryException {
        Student student = users.findStudent(studentId);
        if (student == null) {
            throw new LibraryException("Student not found.");
        }
        return student;
    }

    private Book requireBook(String bookId) throws LibraryException {
        Book book = books.findById(bookId);
        if (book == null) {
            throw new LibraryException("Book not found.");
        }
        return book;
    }

    private Transaction log(TransactionType type, Book book, Student student) {
        Transaction transaction = new Transaction(transactions.nextId(), type, book, student, LocalDate.now());
        transactions.save(transaction);
        student.getHistory().addLast(transaction);
        return transaction;
    }

    private BorrowRecord activeLoan(Student student, Book book) {
        for (BorrowRecord record : records.findByStudent(student.getId())) {
            if (!record.isReturned() && record.getBook() == book) {
                return record;
            }
        }
        return null;
    }

    public BorrowRecord borrow(String studentId, String bookId) throws LibraryException {
        Student student = requireStudent(studentId);
        Book book = requireBook(bookId);
        if (activeLoan(student, book) != null) {
            throw new LibraryException("You already borrowed this book.");
        }
        if (countActiveLoans(student.getId()) >= MAX_ACTIVE_LOANS) {
            throw new LibraryException("Borrowing limit reached (" + MAX_ACTIVE_LOANS + " books).");
        }
        if (!book.isAvailable()) {
            throw new LibraryException("No copies are available right now. You can reserve this book.");
        }
        boolean fromQueue = false;
        if (!book.getReservations().isEmpty()) {
            if (!book.getReservations().peek().equals(student.getId())) {
                throw new LibraryException("This book is on hold for the next student in the reservation queue.");
            }
            book.getReservations().dequeue();
            fromQueue = true;
        }
        book.checkOut();
        LocalDate today = LocalDate.now();
        BorrowRecord record = new BorrowRecord(records.nextId(), book, student, today, today.plusDays(LOAN_DAYS));
        records.save(record);
        Transaction transaction = log(TransactionType.BORROW, book, student);
        undoManager.record(new BorrowAction(student, book, transaction, transactions, record, records, fromQueue));
        store.save();
        return record;
    }

    public BorrowRecord returnBook(int recordId) throws LibraryException {
        BorrowRecord record = records.findById(recordId);
        if (record == null) {
            throw new LibraryException("Borrow record not found.");
        }
        if (record.isReturned()) {
            throw new LibraryException("This book was already returned.");
        }
        record.markReturned(LocalDate.now());
        record.getBook().checkIn();
        Transaction transaction = log(TransactionType.RETURN, record.getBook(), record.getStudent());
        undoManager.record(new ReturnAction(record.getStudent(), record.getBook(), transaction, transactions, record));
        store.save();
        return record;
    }

    public int reserve(String studentId, String bookId) throws LibraryException {
        Student student = requireStudent(studentId);
        Book book = requireBook(bookId);
        if (book.isAvailable() && book.getReservations().isEmpty()) {
            throw new LibraryException("This book is available. Borrow it instead.");
        }
        if (activeLoan(student, book) != null) {
            throw new LibraryException("You currently have this book.");
        }
        if (book.getReservations().contains(student.getId())) {
            throw new LibraryException("You are already in the reservation queue for this book.");
        }
        book.getReservations().enqueue(student.getId());
        Transaction transaction = log(TransactionType.RESERVE, book, student);
        undoManager.record(new ReserveAction(student, book, transaction, transactions));
        store.save();
        return book.getReservations().positionOf(student.getId());
    }

    public void cancelReservation(String studentId, String bookId) throws LibraryException {
        Student student = requireStudent(studentId);
        Book book = requireBook(bookId);
        if (!book.getReservations().remove(student.getId())) {
            throw new LibraryException("No reservation found for this book.");
        }
        store.save();
    }

    public String undoLast() throws LibraryException {
        UndoableAction action = undoManager.popLast();
        if (action == null) {
            throw new LibraryException("Nothing to undo.");
        }
        try {
            action.undo();
        } catch (IllegalStateException e) {
            throw new LibraryException("Cannot undo: " + e.getMessage());
        }
        store.save();
        return action.describe();
    }

    public String[] recentActions() {
        return undoManager.recentDescriptions();
    }

    public int countActiveLoans(String studentId) {
        int count = 0;
        for (BorrowRecord record : records.findByStudent(studentId)) {
            if (!record.isReturned()) {
                count++;
            }
        }
        return count;
    }

    public int countOverdue(String studentId) {
        int count = 0;
        LocalDate today = LocalDate.now();
        for (BorrowRecord record : records.findByStudent(studentId)) {
            if (record.isOverdue(today)) {
                count++;
            }
        }
        return count;
    }

    public int countPendingReservations(String studentId) {
        int count = 0;
        for (Book book : books.findAll()) {
            if (book.getReservations().contains(studentId)) {
                count++;
            }
        }
        return count;
    }

    public BorrowRecord[] activeLoansOf(String studentId) {
        MyLinkedList<BorrowRecord> result = new MyLinkedList<>();
        for (BorrowRecord record : records.findByStudent(studentId)) {
            if (!record.isReturned()) {
                result.add(record);
            }
        }
        return sortRecords(result, RecordSort.OLDEST);
    }

    public BorrowRecord[] returnedHistoryOf(String studentId) {
        MyLinkedList<BorrowRecord> result = new MyLinkedList<>();
        for (BorrowRecord record : records.findByStudent(studentId)) {
            if (record.isReturned()) {
                result.add(record);
            }
        }
        return sortRecords(result, RecordSort.NEWEST);
    }

    public BorrowRecord[] allRecords(RecordSort sort) {
        MyLinkedList<BorrowRecord> result = new MyLinkedList<>();
        for (BorrowRecord record : records.findAll()) {
            result.add(record);
        }
        return sortRecords(result, sort);
    }

    public BorrowRecord[] allActiveRecords(RecordSort sort) {
        return sortRecords(records.findActive(), sort);
    }

    private BorrowRecord[] sortRecords(MyLinkedList<BorrowRecord> list, RecordSort sort) {
        BorrowRecord[] array = list.toArray(new BorrowRecord[0]);
        MergeSort.sort(array, sort.comparator());
        return array;
    }

    public Book[] pendingReservationsOf(String studentId) {
        MyLinkedList<Book> result = new MyLinkedList<>();
        for (Book book : books.findAll()) {
            if (book.getReservations().contains(studentId)) {
                result.add(book);
            }
        }
        return result.toArray(new Book[0]);
    }

    public Transaction[] transactionsOf(Student student, TransactionSort sort) {
        Transaction[] array = student.getHistory().toArray(new Transaction[0]);
        MergeSort.sort(array, sort.comparator());
        return array;
    }
}
