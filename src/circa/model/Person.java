package circa.model;

public abstract class Person {
    private final String id;
    private final String name;
    private String passwordHash;

    protected Person(String id, String name, String secret, boolean alreadyHashed) {
        this.id = id.trim().toUpperCase();
        this.name = name.trim();
        this.passwordHash = alreadyHashed ? secret : PasswordHasher.hash(secret);
    }

    public abstract UserRole getRole();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean checkPassword(String attempt) {
        return passwordHash.equals(PasswordHasher.hash(attempt));
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void changePassword(String newPassword) {
        this.passwordHash = PasswordHasher.hash(newPassword);
    }
}
