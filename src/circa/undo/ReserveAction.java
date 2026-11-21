package circa.undo;

import circa.model.Book;
import circa.model.Student;
import circa.model.Transaction;
import circa.repository.TransactionRepository;

public class ReserveAction extends TransactionAction {
    public ReserveAction(Student student, Book book, Transaction transaction,
                         TransactionRepository transactionRepository) {
        super(student, book, transaction, transactionRepository);
    }

    @Override
    protected void revert() {
        getBook().getReservations().remove(getStudent().getId());
    }
}
