package circa.controller;

import circa.model.Book;
import circa.model.BorrowRecord;
import circa.model.Student;
import circa.model.Transaction;
import circa.service.BookService;
import circa.service.BookSort;
import circa.service.BorrowingService;
import circa.service.LibraryException;
import circa.service.TransactionSort;

public class StudentController {
    private final BookService bookService;
    private final BorrowingService borrowingService;

    public StudentController(BookService bookService, BorrowingService borrowingService) {
        this.bookService = bookService;
        this.borrowingService = borrowingService;
    }

    public Book[] searchBooks(String keyword, String category, BookSort sort) {
        return bookService.search(keyword, category, sort);
    }

    public String[] categories() {
        return bookService.categories();
    }

    public Book[] recentBooks(int count) {
        return bookService.recentlyAdded(count);
    }

    public Book findBook(String bookId) {
        return bookService.findById(bookId);
    }

    public int totalAvailableCopies() {
        return bookService.countAvailableCopies();
    }

    public int activeLoanCount(Student student) {
        return borrowingService.countActiveLoans(student.getId());
    }

    public int overdueCount(Student student) {
        return borrowingService.countOverdue(student.getId());
    }

    public int pendingRequestCount(Student student) {
        return borrowingService.countPendingReservations(student.getId());
    }

    public Result<BorrowRecord> borrow(Student student, String bookId) {
        try {
            BorrowRecord record = borrowingService.borrow(student.getId(), bookId);
            return Result.ok("Borrowed \"" + record.getBook().getTitle() + "\". Due on "
                    + record.getDueDate() + ".", record);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<Integer> reserve(Student student, String bookId) {
        try {
            int position = borrowingService.reserve(student.getId(), bookId);
            return Result.ok("Reservation placed. Your position in the queue: " + position + ".", position);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<Void> cancelReservation(Student student, String bookId) {
        try {
            borrowingService.cancelReservation(student.getId(), bookId);
            return Result.ok("Reservation cancelled.", null);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<BorrowRecord> returnBook(int recordId) {
        try {
            BorrowRecord record = borrowingService.returnBook(recordId);
            return Result.ok("Returned \"" + record.getBook().getTitle() + "\".", record);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public BorrowRecord[] activeLoans(Student student) {
        return borrowingService.activeLoansOf(student.getId());
    }

    public BorrowRecord[] returnedHistory(Student student) {
        return borrowingService.returnedHistoryOf(student.getId());
    }

    public Book[] pendingReservations(Student student) {
        return borrowingService.pendingReservationsOf(student.getId());
    }

    public int queuePosition(Student student, Book book) {
        return book.getReservations().positionOf(student.getId());
    }

    public Transaction[] transactions(Student student, TransactionSort sort) {
        return borrowingService.transactionsOf(student, sort);
    }

    public void resetHistoryCursor(Student student) {
        student.getHistory().moveToLast();
    }

    public boolean moveHistoryPrevious(Student student) {
        return student.getHistory().moveToPrevious();
    }

    public boolean moveHistoryNext(Student student) {
        return student.getHistory().moveToNext();
    }

    public Transaction currentTransaction(Student student) {
        return student.getHistory().current();
    }

    public int historyPosition(Student student) {
        return student.getHistory().currentPosition();
    }

    public int historySize(Student student) {
        return student.getHistory().size();
    }
}
