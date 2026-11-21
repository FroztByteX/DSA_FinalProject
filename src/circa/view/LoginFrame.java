package circa.view;

import circa.controller.AuthController;
import circa.controller.Result;
import circa.model.Person;
import circa.model.Student;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginFrame extends JFrame {
    private final AuthController controller;
    private final Consumer<Person> onSuccess;
    private final JTextField idField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public LoginFrame(AuthController controller, Consumer<Person> onSuccess) {
        super("CIRCA - Login");
        this.controller = controller;
        this.onSuccess = onSuccess;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
<<<<<<< HEAD
        JLabel logo = Widgets.logo(120, 120);
=======
        JLabel logo = Widgets.imagePlaceholder(100, 100);
>>>>>>> 1a93c7cd73cd4ada2f0ab6542ebd5310c69289ca
        logo.setAlignmentX(CENTER_ALIGNMENT);
        JLabel title = new JLabel("CIRCA: LIBRARY RESERVATION", SwingConstants.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("AND BORROWING SYSTEM", SwingConstants.CENTER);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);
        top.add(logo);
        top.add(Box.createVerticalStrut(10));
        top.add(title);
        top.add(subtitle);

        JButton loginButton = new JButton("LOGIN");
        JButton forgotButton = new JButton("Forgot Password?");
        JButton registerButton = new JButton("Register / Create Account");

        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.add(new JLabel("Username / Student ID"));
        form.add(idField);
        form.add(new JLabel("Password"));
        form.add(passwordField);
        form.add(loginButton);
        form.add(forgotButton);
        form.add(new JSeparator());
        form.add(new JLabel("New here?", SwingConstants.CENTER));
        form.add(registerButton);

        root.add(top, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        add(root);

        loginButton.addActionListener(e -> attemptLogin());
        passwordField.addActionListener(e -> attemptLogin());
        forgotButton.addActionListener(e -> Messages.info(this,
                "Please ask a librarian to reset your password."));
        registerButton.addActionListener(e -> attemptRegister());

        setSize(420, 560);
        setLocationRelativeTo(null);
    }

    private void attemptLogin() {
        Result<Person> result = controller.login(idField.getText(), new String(passwordField.getPassword()));
        if (!result.isSuccess()) {
            Messages.show(this, result);
            return;
        }
        dispose();
        onSuccess.accept(result.getValue());
    }

    private void attemptRegister() {
        RegisterDialog dialog = new RegisterDialog(this, "Create Account");
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) {
            return;
        }
        Result<Student> result = controller.register(dialog.getStudentId(), dialog.getFullName(), dialog.getPassword());
        Messages.show(this, result);
        if (result.isSuccess()) {
            idField.setText(result.getValue().getId());
            passwordField.setText("");
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 1a93c7cd73cd4ada2f0ab6542ebd5310c69289ca
