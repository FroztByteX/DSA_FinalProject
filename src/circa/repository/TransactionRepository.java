package circa.repository;

import circa.model.Transaction;
import circa.structures.MyHashTable;
import circa.structures.MyLinkedList;

public class TransactionRepository implements Repository<Integer, Transaction> {
    private final MyHashTable<Integer, Transaction> index = new MyHashTable<>();
    private final MyLinkedList<Transaction> transactions = new MyLinkedList<>();
    private int nextId = 1;

    public int nextId() {
        return nextId++;
    }

    @Override
    public void save(Transaction transaction) {
        if (index.containsKey(transaction.getId())) {
            return;
        }
        index.put(transaction.getId(), transaction);
        transactions.add(transaction);
        nextId = Math.max(nextId, transaction.getId() + 1);
    }

    @Override
    public Transaction findById(Integer id) {
        return index.get(id);
    }

    @Override
    public boolean deleteById(Integer id) {
        Transaction transaction = index.remove(id);
        if (transaction == null) {
            return false;
        }
        transactions.remove(transaction);
        return true;
    }

    @Override
    public Iterable<Transaction> findAll() {
        return transactions;
    }

    @Override
    public int count() {
        return transactions.size();
    }
}
