package circa;

import circa.model.Book;
import circa.model.Librarian;
import circa.model.Student;
import circa.repository.BookRepository;
import circa.repository.UserRepository;

public final class SampleData {
    private SampleData() {
    }

    public static void load(UserRepository users, BookRepository books) {
        users.save(new Librarian("LIB001", "Head Librarian", "admin123"));
        users.save(new Student("STU001", "Student One", "pass1"));
        users.save(new Student("STU002", "Student Two", "pass2"));
        users.save(new Student("STU003", "Student Three", "pass3"));

        books.save(new Book("B001", "Introduction to Algorithms", "Thomas Cormen", "9780262046305", "Computer Science", 2));
        books.save(new Book("B002", "Clean Code", "Robert Martin", "9780132350884", "Programming", 1));
        books.save(new Book("B003", "Data Structures in Java", "Mark Weiss", "9780132576277", "Computer Science", 3));
        books.save(new Book("B004", "Noli Me Tangere", "Jose Rizal", "9789712715068", "Literature", 2));
        books.save(new Book("B005", "El Filibusterismo", "Jose Rizal", "9789712715075", "Literature", 1));
        books.save(new Book("B006", "Database System Concepts", "Abraham Silberschatz", "9780078022159", "Computer Science", 2));
        books.save(new Book("B007", "The Pragmatic Programmer", "Andrew Hunt", "9780135957059", "Programming", 1));
        books.save(new Book("B008", "Physics for Scientists", "Raymond Serway", "9781337553292", "Science", 2));
        books.save(new Book("B009", "Philippine History", "Teodoro Agoncillo", "9789715068925", "History", 1));
        books.save(new Book("B010", "Calculus Early Transcendentals", "James Stewart", "9781285741550", "Mathematics", 2));
    }
}
