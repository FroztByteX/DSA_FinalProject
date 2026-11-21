package circa.service;

import circa.model.BorrowRecord;
import java.util.Comparator;

public enum RecordSort {
    NEWEST("Newest first", Comparator.comparing((BorrowRecord r) -> r.getBorrowDate())
            .thenComparingInt((BorrowRecord r) -> r.getId()).reversed()),
    OLDEST("Oldest first", Comparator.comparing((BorrowRecord r) -> r.getBorrowDate())
            .thenComparingInt((BorrowRecord r) -> r.getId())),
    STUDENT("Student", Comparator.comparing((BorrowRecord r) -> r.getStudent().getId())
            .thenComparingInt((BorrowRecord r) -> r.getId())),
    BOOK("Book title", Comparator.comparing((BorrowRecord r) -> r.getBook().getTitle().toLowerCase())
            .thenComparingInt((BorrowRecord r) -> r.getId()));

    private final String label;
    private final Comparator<BorrowRecord> comparator;

    RecordSort(String label, Comparator<BorrowRecord> comparator) {
        this.label = label;
        this.comparator = comparator;
    }

    public Comparator<BorrowRecord> comparator() {
        return comparator;
    }

    @Override
    public String toString() {
        return label;
    }
}
