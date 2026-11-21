package circa.controller;

import circa.model.Book;
import circa.model.BorrowRecord;
import circa.model.Student;
import circa.service.BookService;
import circa.service.BookSort;
import circa.service.BorrowingService;
import circa.service.LibraryException;
import circa.service.RecordSort;
import circa.service.UserService;

public class AdminController {
    private final BookService bookService;
    private final BorrowingService borrowingService;
    private final UserService userService;

    public AdminController(BookService bookService, BorrowingService borrowingService, UserService userService) {
        this.bookService = bookService;
        this.borrowingService = borrowingService;
        this.userService = userService;
    }

    public Book[] searchInventory(String keyword, BookSort sort) {
        return bookService.search(keyword, null, sort);
    }

    public Result<Void> addBook(String id, String title, String author, String isbn, String category, int copies) {
        try {
            bookService.addBook(id, title, author, isbn, category, copies);
            return Result.ok("Book added.", null);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<Void> updateBook(String id, String title, String author, String isbn, String category, int copies) {
        try {
            bookService.updateBook(id, title, author, isbn, category, copies);
            return Result.ok("Book updated.", null);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<Void> deleteBook(String id) {
        try {
            bookService.deleteBook(id);
            return Result.ok("Book deleted.", null);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Student[] students() {
        return userService.listStudents();
    }

    public int activeLoanCount(Student student) {
        return borrowingService.countActiveLoans(student.getId());
    }

    public Result<Student> addStudent(String id, String name, String password) {
        try {
            Student student = userService.registerStudent(id, name, password);
            return Result.ok("Student added.", student);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<Void> resetPassword(String studentId, String newPassword) {
        try {
            userService.resetPassword(studentId, newPassword);
            return Result.ok("Password reset.", null);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<Void> removeStudent(String studentId) {
        try {
            userService.removeStudent(studentId);
            return Result.ok("Student removed.", null);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public BorrowRecord[] allRecords(RecordSort sort) {
        return borrowingService.allRecords(sort);
    }

    public BorrowRecord[] activeRecords(RecordSort sort) {
        return borrowingService.allActiveRecords(sort);
    }

    public Result<BorrowRecord> issueBook(String studentId, String bookId) {
        try {
            BorrowRecord record = borrowingService.borrow(studentId, bookId);
            return Result.ok("Issued \"" + record.getBook().getTitle() + "\" to " + record.getStudent().getId()
                    + ". Due on " + record.getDueDate() + ".", record);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<BorrowRecord> processReturn(int recordId) {
        try {
            BorrowRecord record = borrowingService.returnBook(recordId);
            return Result.ok("Return processed for \"" + record.getBook().getTitle() + "\".", record);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<String> undoLast() {
        try {
            return Result.ok("Undone: " + borrowingService.undoLast(), null);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public String[] recentActions() {
        return borrowingService.recentActions();
    }
}
