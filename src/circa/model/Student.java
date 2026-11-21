package circa.model;

import circa.structures.MyDoublyLinkedList;

public class Student extends Person {
    private final MyDoublyLinkedList<Transaction> history = new MyDoublyLinkedList<>();

    public Student(String id, String name, String password) {
        super(id, name, password, false);
    }

    public Student(String id, String name, String secret, boolean alreadyHashed) {
        super(id, name, secret, alreadyHashed);
    }

    @Override
    public UserRole getRole() {
        return UserRole.STUDENT;
    }

    public MyDoublyLinkedList<Transaction> getHistory() {
        return history;
    }
}
