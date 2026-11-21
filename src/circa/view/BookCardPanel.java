package circa.view;

import circa.model.Book;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BookCardPanel extends JPanel {
    private final Book book;

    public BookCardPanel(Book book) {
        this.book = book;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(Widgets.imagePlaceholder(150, 110), BorderLayout.NORTH);
        JPanel info = new JPanel(new GridLayout(0, 1));
        info.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        addInfo(info);
        add(info, BorderLayout.CENTER);
    }

    protected void addInfo(JPanel info) {
        info.add(new JLabel(book.getTitle()));
        info.add(new JLabel(book.getAuthor()));
    }

    protected Book getBook() {
        return book;
    }
}
