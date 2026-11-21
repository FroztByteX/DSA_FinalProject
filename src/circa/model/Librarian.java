package circa.model;

public class Librarian extends Person {
    public Librarian(String id, String name, String password) {
        super(id, name, password, false);
    }

    public Librarian(String id, String name, String secret, boolean alreadyHashed) {
        super(id, name, secret, alreadyHashed);
    }

    @Override
    public UserRole getRole() {
        return UserRole.LIBRARIAN;
    }
}
