package circa.undo;

public interface UndoableAction {
    void undo();

    String describe();
}
