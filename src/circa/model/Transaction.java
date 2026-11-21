package circa.model;

import java.time.LocalDate;

public class Transaction {
    private final int id;
    private final TransactionType type;
    private final Book book;
    private final Student student;
    private final LocalDate date;

    public Transaction(int id, TransactionType type, Book book, Student student, LocalDate date) {
        this.id = id;
        this.type = type;
        this.book = book;
        this.student = student;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public Book getBook() {
        return book;
    }

    public Student getStudent() {
        return student;
    }

    public LocalDate getDate() {
        return date;
    }
}
