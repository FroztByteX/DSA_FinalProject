package circa.repository;

public interface Repository<K, T> {
    void save(T item);

    T findById(K id);

    boolean deleteById(K id);

    Iterable<T> findAll();

    int count();
}
