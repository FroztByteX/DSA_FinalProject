package circa.storage;

public interface DataStore {
    boolean hasSavedData();

    void load();

    void save();
}
