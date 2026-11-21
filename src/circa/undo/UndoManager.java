package circa.undo;

import circa.structures.MyStack;

public class UndoManager {
    private final MyStack<UndoableAction> actions = new MyStack<>();

    public void record(UndoableAction action) {
        actions.push(action);
    }

    public UndoableAction popLast() {
        return actions.pop();
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }

    public String[] recentDescriptions() {
        String[] result = new String[actions.size()];
        int i = 0;
        for (UndoableAction action : actions) {
            result[i++] = action.describe();
        }
        return result;
    }
}
