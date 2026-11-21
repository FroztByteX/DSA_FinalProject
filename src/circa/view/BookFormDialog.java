package circa.view;

import circa.model.Book;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookFormDialog extends JDialog {
    private final JTextField idField = new JTextField(18);
    private final JTextField titleField = new JTextField(18);
    private final JTextField authorField = new JTextField(18);
    private final JTextField isbnField = new JTextField(18);
    private final JTextField categoryField = new JTextField(18);
    private final JTextField copiesField = new JTextField(18);
    private boolean confirmed;

    public BookFormDialog(Window owner, Book existing) {
        super(owner, existing == null ? "Add Book" : "Edit Book", ModalityType.APPLICATION_MODAL);
        if (existing != null) {
            idField.setText(existing.getId());
            idField.setEditable(false);
            titleField.setText(existing.getTitle());
            authorField.setText(existing.getAuthor());
            isbnField.setText(existing.getIsbn());
            categoryField.setText(existing.getCategory());
            copiesField.setText(String.valueOf(existing.getTotalCopies()));
        } else {
            copiesField.setText("1");
        }

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        form.add(new JLabel("Book ID:"));
        form.add(idField);
        form.add(new JLabel("Title:"));
        form.add(titleField);
        form.add(new JLabel("Author:"));
        form.add(authorField);
        form.add(new JLabel("ISBN:"));
        form.add(isbnField);
        form.add(new JLabel("Category:"));
        form.add(categoryField);
        form.add(new JLabel("Copies:"));
        form.add(copiesField);

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        save.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        cancel.addActionListener(e -> dispose());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(save);
        buttons.add(cancel);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getBookId() {
        return idField.getText();
    }

    public String getBookTitle() {
        return titleField.getText();
    }

    public String getAuthor() {
        return authorField.getText();
    }

    public String getIsbn() {
        return isbnField.getText();
    }

    public String getCategory() {
        return categoryField.getText();
    }

    public String getCopiesText() {
        return copiesField.getText();
    }
}
