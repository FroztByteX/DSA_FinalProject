package circa.service;

import circa.model.Transaction;
import java.util.Comparator;

public enum TransactionSort {
    NEWEST("Newest first", Comparator.comparing((Transaction t) -> t.getDate())
            .thenComparingInt((Transaction t) -> t.getId()).reversed()),
    OLDEST("Oldest first", Comparator.comparing((Transaction t) -> t.getDate())
            .thenComparingInt((Transaction t) -> t.getId())),
    TYPE("Type", Comparator.comparing((Transaction t) -> t.getType().name())
            .thenComparingInt((Transaction t) -> t.getId())),
    BOOK("Book title", Comparator.comparing((Transaction t) -> t.getBook().getTitle().toLowerCase())
            .thenComparingInt((Transaction t) -> t.getId()));

    private final String label;
    private final Comparator<Transaction> comparator;

    TransactionSort(String label, Comparator<Transaction> comparator) {
        this.label = label;
        this.comparator = comparator;
    }

    public Comparator<Transaction> comparator() {
        return comparator;
    }

    @Override
    public String toString() {
        return label;
    }
}
