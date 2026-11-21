package circa.undo;

import circa.model.Book;
import circa.model.Student;
import circa.model.Transaction;
import circa.repository.TransactionRepository;

public abstract class TransactionAction implements UndoableAction {
    private final Student student;
    private final Book book;
    private final Transaction transaction;
    private final TransactionRepository transactionRepository;

    protected TransactionAction(Student student, Book book, Transaction transaction,
                                TransactionRepository transactionRepository) {
        this.student = student;
        this.book = book;
        this.transaction = transaction;
        this.transactionRepository = transactionRepository;
    }

    protected abstract void revert();

    @Override
    public final void undo() {
        revert();
        student.getHistory().remove(transaction);
        transactionRepository.deleteById(transaction.getId());
    }

    @Override
    public String describe() {
        return transaction.getType() + " | " + book.getId() + " " + book.getTitle() + " | " + student.getId();
    }

    protected Student getStudent() {
        return student;
    }

    protected Book getBook() {
        return book;
    }
}
