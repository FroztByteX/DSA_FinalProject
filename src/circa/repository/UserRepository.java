package circa.repository;

import circa.model.Person;
import circa.model.Student;
import circa.structures.MyHashTable;
import circa.structures.MyLinkedList;

public class UserRepository implements Repository<String, Person> {
    private final MyHashTable<String, Person> index = new MyHashTable<>();
    private final MyLinkedList<Person> users = new MyLinkedList<>();

    @Override
    public void save(Person person) {
        if (index.containsKey(person.getId())) {
            return;
        }
        index.put(person.getId(), person);
        users.add(person);
    }

    @Override
    public Person findById(String id) {
        return id == null ? null : index.get(id.trim().toUpperCase());
    }

    public Student findStudent(String id) {
        Person person = findById(id);
        return person instanceof Student ? (Student) person : null;
    }

    @Override
    public boolean deleteById(String id) {
        Person person = findById(id);
        if (person == null) {
            return false;
        }
        index.remove(person.getId());
        users.remove(person);
        return true;
    }

    @Override
    public Iterable<Person> findAll() {
        return users;
    }

    public MyLinkedList<Student> findStudents() {
        MyLinkedList<Student> students = new MyLinkedList<>();
        for (Person person : users) {
            if (person instanceof Student) {
                students.add((Student) person);
            }
        }
        return students;
    }

    @Override
    public int count() {
        return users.size();
    }
}
