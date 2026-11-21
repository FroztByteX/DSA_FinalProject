package circa.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterDialog extends JDialog {
    private final JTextField idField = new JTextField(15);
    private final JTextField nameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private boolean confirmed;

    public RegisterDialog(Window owner, String heading) {
        super(owner, heading, ModalityType.APPLICATION_MODAL);
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        form.add(new JLabel("Student ID:"));
        form.add(idField);
        form.add(new JLabel("Full name:"));
        form.add(nameField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);

        JButton ok = new JButton("Create Account");
        JButton cancel = new JButton("Cancel");
        ok.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        cancel.addActionListener(e -> dispose());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(ok);
        buttons.add(cancel);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getStudentId() {
        return idField.getText();
    }

    public String getFullName() {
        return nameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }
}
