package circa;

import circa.controller.AdminController;
import circa.controller.AuthController;
import circa.controller.StudentController;
import circa.model.Librarian;
import circa.model.Person;
import circa.model.Student;
import circa.repository.BookRepository;
import circa.repository.BorrowRecordRepository;
import circa.repository.TransactionRepository;
import circa.repository.UserRepository;
import circa.service.BookService;
import circa.service.BorrowingService;
import circa.service.UserService;
import circa.storage.DataStore;
import circa.storage.FileDataStore;
import circa.storage.StorageException;
import circa.undo.UndoManager;
import circa.view.AdminFrame;
import circa.view.LoginFrame;
import circa.view.Messages;
import circa.view.StudentFrame;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppContext {
    private static final Path DATA_DIRECTORY = Paths.get("data");

    private AuthController authController;
    private StudentController studentController;
    private AdminController adminController;
    private String startupWarning;

    public AppContext() {
        try {
            build(true);
        } catch (StorageException e) {
            startupWarning = e.getMessage() + "\nThe unreadable files were moved to "
                    + backUpCorruptData() + ". Starting with fresh sample data.";
            build(false);
        }
    }

    private void build(boolean useSavedData) {
        BookRepository bookRepository = new BookRepository();
        UserRepository userRepository = new UserRepository();
        BorrowRecordRepository recordRepository = new BorrowRecordRepository();
        TransactionRepository transactionRepository = new TransactionRepository();
        DataStore store = new FileDataStore(DATA_DIRECTORY, userRepository, bookRepository,
                recordRepository, transactionRepository);

        if (useSavedData && store.hasSavedData()) {
            store.load();
        } else {
            SampleData.load(userRepository, bookRepository);
            store.save();
        }

        UndoManager undoManager = new UndoManager();
        BookService bookService = new BookService(bookRepository, store);
        UserService userService = new UserService(userRepository, recordRepository, store);
        BorrowingService borrowingService = new BorrowingService(
                bookRepository, userRepository, recordRepository, transactionRepository, undoManager, store);

        authController = new AuthController(userService);
        studentController = new StudentController(bookService, borrowingService);
        adminController = new AdminController(bookService, borrowingService, userService);
    }

    private String backUpCorruptData() {
        Path backup = DATA_DIRECTORY.resolveSibling("data_unreadable_" + System.currentTimeMillis());
        try {
            Files.move(DATA_DIRECTORY, backup);
            return backup.toString();
        } catch (IOException e) {
            return "(could not move them: " + e.getMessage() + ")";
        }
    }

    public void start() {
        if (startupWarning != null) {
            Messages.info(null, startupWarning);
        }
        showLogin();
    }

    private void showLogin() {
        new LoginFrame(authController, this::openMainWindow).setVisible(true);
    }

    private void openMainWindow(Person person) {
        switch (person.getRole()) {
            case STUDENT:
                new StudentFrame((Student) person, studentController, this::showLogin).setVisible(true);
                break;
            case LIBRARIAN:
                new AdminFrame((Librarian) person, adminController, this::showLogin).setVisible(true);
                break;
            default:
                break;
        }
    }
}
