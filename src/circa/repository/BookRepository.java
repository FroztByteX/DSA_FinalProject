package circa.repository;

import circa.model.Book;
import circa.structures.BinarySearchTree;
import circa.structures.MyHashTable;
import circa.structures.MyLinkedList;

public class BookRepository implements Repository<String, Book> {
    private final MyHashTable<String, Book> index = new MyHashTable<>();
    private final MyLinkedList<Book> books = new MyLinkedList<>();
    private final BinarySearchTree<Book> titleTree = new BinarySearchTree<>(
            (a, b) -> {
                int result = a.getTitle().compareToIgnoreCase(b.getTitle());
                return result != 0 ? result : a.getId().compareTo(b.getId());
            });

    @Override
    public void save(Book book) {
        if (index.containsKey(book.getId())) {
            return;
        }
        index.put(book.getId(), book);
        books.add(book);
        titleTree.insert(book);
    }

    @Override
    public Book findById(String id) {
        return id == null ? null : index.get(id.trim().toUpperCase());
    }

    @Override
    public boolean deleteById(String id) {
        Book book = findById(id);
        if (book == null) {
            return false;
        }
        index.remove(book.getId());
        books.remove(book);
        reindex();
        return true;
    }

    @Override
    public Iterable<Book> findAll() {
        return books;
    }

    public Iterable<Book> findAllByTitle() {
        return titleTree.inOrder();
    }

    public Book[] findMostRecent(int count) {
        Book[] all = books.toArray(new Book[0]);
        int size = Math.min(count, all.length);
        Book[] result = new Book[size];
        for (int i = 0; i < size; i++) {
            result[i] = all[all.length - 1 - i];
        }
        return result;
    }

    public void reindex() {
        titleTree.rebuild(books);
    }

    @Override
    public int count() {
        return books.size();
    }
}
