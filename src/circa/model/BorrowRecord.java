package circa.model;

import java.time.LocalDate;

public class BorrowRecord {
    private final int id;
    private final Book book;
    private final Student student;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public BorrowRecord(int id, Book book, Student student, LocalDate borrowDate, LocalDate dueDate) {
        this.id = id;
        this.book = book;
        this.student = student;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Student getStudent() {
        return student;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public boolean isOverdue(LocalDate today) {
        return !isReturned() && today.isAfter(dueDate);
    }

    public void markReturned(LocalDate date) {
        this.returnDate = date;
    }

    public void clearReturn() {
        this.returnDate = null;
    }

    public String getStatus(LocalDate today) {
        if (isReturned()) {
            return "Returned";
        }
        return isOverdue(today) ? "Overdue" : "Borrowed";
    }

    public String getDetails() {
        if (isReturned() && returnDate.isAfter(dueDate)) {
            return "Returned late";
        }
        return "-";
    }
}
