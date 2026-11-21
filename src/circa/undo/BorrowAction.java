package circa.undo;

import circa.model.Book;
import circa.model.BorrowRecord;
import circa.model.Student;
import circa.model.Transaction;
import circa.repository.BorrowRecordRepository;
import circa.repository.TransactionRepository;

public class BorrowAction extends TransactionAction {
    private final BorrowRecord record;
    private final BorrowRecordRepository recordRepository;
    private final boolean fromQueue;

    public BorrowAction(Student student, Book book, Transaction transaction, TransactionRepository transactionRepository,
                        BorrowRecord record, BorrowRecordRepository recordRepository, boolean fromQueue) {
        super(student, book, transaction, transactionRepository);
        this.record = record;
        this.recordRepository = recordRepository;
        this.fromQueue = fromQueue;
    }

    @Override
    protected void revert() {
        getBook().checkIn();
        if (fromQueue) {
            getBook().getReservations().addFirst(getStudent().getId());
        }
        recordRepository.deleteById(record.getId());
    }
}
