package circa.repository;

import circa.model.BorrowRecord;
import circa.structures.MyHashTable;
import circa.structures.MyLinkedList;

public class BorrowRecordRepository implements Repository<Integer, BorrowRecord> {
    private final MyHashTable<Integer, BorrowRecord> index = new MyHashTable<>();
    private final MyLinkedList<BorrowRecord> records = new MyLinkedList<>();
    private int nextId = 1;

    public int nextId() {
        return nextId++;
    }

    @Override
    public void save(BorrowRecord record) {
        if (index.containsKey(record.getId())) {
            return;
        }
        index.put(record.getId(), record);
        records.add(record);
        nextId = Math.max(nextId, record.getId() + 1);
    }

    @Override
    public BorrowRecord findById(Integer id) {
        return index.get(id);
    }

    @Override
    public boolean deleteById(Integer id) {
        BorrowRecord record = index.remove(id);
        if (record == null) {
            return false;
        }
        records.remove(record);
        return true;
    }

    @Override
    public Iterable<BorrowRecord> findAll() {
        return records;
    }

    public MyLinkedList<BorrowRecord> findByStudent(String studentId) {
        MyLinkedList<BorrowRecord> result = new MyLinkedList<>();
        for (BorrowRecord record : records) {
            if (record.getStudent().getId().equals(studentId)) {
                result.add(record);
            }
        }
        return result;
    }

    public MyLinkedList<BorrowRecord> findActive() {
        MyLinkedList<BorrowRecord> result = new MyLinkedList<>();
        for (BorrowRecord record : records) {
            if (!record.isReturned()) {
                result.add(record);
            }
        }
        return result;
    }

    @Override
    public int count() {
        return records.size();
    }
}
