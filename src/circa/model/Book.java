package circa.model;

import circa.structures.MyQueue;

public class Book {
    private final String id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private int totalCopies;
    private int availableCopies;
    private final MyQueue<String> reservations = new MyQueue<>();

    public Book(String id, String title, String author, String isbn, String category, int totalCopies) {
        this.id = id.trim().toUpperCase();
        this.title = title.trim();
        this.author = author.trim();
        this.isbn = isbn.trim();
        this.category = category.trim();
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getCategory() {
        return category;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public int getBorrowedCopies() {
        return totalCopies - availableCopies;
    }

    public MyQueue<String> getReservations() {
        return reservations;
    }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    public String getStatus() {
        return isAvailable() ? "Available" : "Borrowed";
    }

    public void updateDetails(String title, String author, String isbn, String category) {
        this.title = title.trim();
        this.author = author.trim();
        this.isbn = isbn.trim();
        this.category = category.trim();
    }

    public void updateTotalCopies(int newTotal) {
        if (newTotal < getBorrowedCopies()) {
            throw new IllegalArgumentException("Total copies cannot be lower than copies currently borrowed.");
        }
        availableCopies += newTotal - totalCopies;
        totalCopies = newTotal;
    }

    public void restoreAvailableCopies(int available) {
        if (available < 0 || available > totalCopies) {
            throw new IllegalArgumentException("Available copies must be between 0 and total copies.");
        }
        this.availableCopies = available;
    }

    public void checkOut() {
        if (availableCopies <= 0) {
            throw new IllegalStateException("No copies available.");
        }
        availableCopies--;
    }

    public void checkIn() {
        if (availableCopies >= totalCopies) {
            throw new IllegalStateException("All copies are already in the library.");
        }
        availableCopies++;
    }
}
