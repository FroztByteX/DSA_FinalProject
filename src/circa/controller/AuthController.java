package circa.controller;

import circa.model.Person;
import circa.model.Student;
import circa.service.LibraryException;
import circa.service.UserService;

public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    public Result<Person> login(String id, String password) {
        try {
            Person person = userService.authenticate(id, password);
            return Result.ok("Welcome, " + person.getName() + ".", person);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<Student> register(String id, String name, String password) {
        try {
            Student student = userService.registerStudent(id, name, password);
            return Result.ok("Account created for " + student.getName() + " (" + student.getId() + ").", student);
        } catch (LibraryException e) {
            return Result.fail(e.getMessage());
        }
    }
}
