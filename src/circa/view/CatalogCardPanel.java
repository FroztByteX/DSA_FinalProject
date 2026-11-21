package circa.view;

import circa.model.Book;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CatalogCardPanel extends BookCardPanel {
    public CatalogCardPanel(Book book, ActionListener onDetails, ActionListener onBorrow) {
        super(book);
        JButton details = new JButton("View Details");
        JButton borrow = new JButton("Borrow");
        details.addActionListener(onDetails);
        borrow.addActionListener(onBorrow);
        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        buttons.add(details);
        buttons.add(borrow);
        add(buttons, BorderLayout.SOUTH);
    }

    @Override
    protected void addInfo(JPanel info) {
        super.addInfo(info);
        info.add(new JLabel("Category: " + getBook().getCategory()));
        info.add(new JLabel("Status: " + getBook().getStatus()
                + " (" + getBook().getAvailableCopies() + "/" + getBook().getTotalCopies() + ")"));
    }
}
