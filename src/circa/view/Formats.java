package circa.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class Formats {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private Formats() {
    }

    public static String date(LocalDate date) {
        return date == null ? "" : DATE.format(date);
    }
}
