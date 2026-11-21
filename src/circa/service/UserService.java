package circa.service;

import circa.model.BorrowRecord;
import circa.model.Person;
import circa.model.Student;
import circa.repository.BorrowRecordRepository;
import circa.repository.UserRepository;
import circa.storage.DataStore;
import circa.structures.MergeSort;

public class UserService {
    private final UserRepository users;
    private final BorrowRecordRepository records;
    private final DataStore store;

    public UserService(UserRepository users, BorrowRecordRepository records, DataStore store) {
        this.users = users;
        this.records = records;
        this.store = store;
    }

    public Person authenticate(String id, String password) throws LibraryException {
        if (id == null || id.isBlank() || password == null || password.isEmpty()) {
            throw new LibraryException("Enter your ID and password.");
        }
        Person person = users.findById(id);
        if (person == null || !person.checkPassword(password)) {
            throw new LibraryException("Invalid ID or password.");
        }
        return person;
    }

    public Student registerStudent(String id, String name, String password) throws LibraryException {
        if (id == null || id.isBlank() || name == null || name.isBlank() || password == null || password.length() < 4) {
            throw new LibraryException("ID and name are required, and the password needs at least 4 characters.");
        }
        if (users.findById(id) != null) {
            throw new LibraryException("That ID is already registered.");
        }
        Student student = new Student(id, name, password);
        users.save(student);
        store.save();
        return student;
    }

    public void resetPassword(String studentId, String newPassword) throws LibraryException {
        Student student = users.findStudent(studentId);
        if (student == null) {
            throw new LibraryException("Student not found.");
        }
        if (newPassword == null || newPassword.length() < 4) {
            throw new LibraryException("The password needs at least 4 characters.");
        }
        student.changePassword(newPassword);
        store.save();
    }

    public void removeStudent(String studentId) throws LibraryException {
        Student student = users.findStudent(studentId);
        if (student == null) {
            throw new LibraryException("Student not found.");
        }
        for (BorrowRecord record : records.findByStudent(student.getId())) {
            if (!record.isReturned()) {
                throw new LibraryException("Cannot remove a student who still has borrowed books.");
            }
        }
        users.deleteById(student.getId());
        store.save();
    }

    public Student[] listStudents() {
        Student[] students = users.findStudents().toArray(new Student[0]);
        MergeSort.sort(students, (a, b) -> a.getId().compareTo(b.getId()));
        return students;
    }
}
