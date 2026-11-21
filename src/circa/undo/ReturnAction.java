package circa.undo;

import circa.model.Book;
import circa.model.BorrowRecord;
import circa.model.Student;
import circa.model.Transaction;
import circa.repository.TransactionRepository;

public class ReturnAction extends TransactionAction {
    private final BorrowRecord record;

    public ReturnAction(Student student, Book book, Transaction transaction, TransactionRepository transactionRepository,
                        BorrowRecord record) {
        super(student, book, transaction, transactionRepository);
        this.record = record;
    }

    @Override
    protected void revert() {
        getBook().checkOut();
        record.clearReturn();
    }
}
