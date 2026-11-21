package circa.storage;

import circa.structures.MyLinkedList;

final class TextRecord {
    private TextRecord() {
    }

    static String join(String... fields) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                line.append(',');
            }
            line.append(quote(fields[i] == null ? "" : fields[i]));
        }
        return line.toString();
    }

    private static String quote(String field) {
        String clean = field.replace("\r", " ").replace("\n", " ");
        if (clean.contains(",") || clean.contains("\"")) {
            return "\"" + clean.replace("\"", "\"\"") + "\"";
        }
        return clean;
    }

    static String[] split(String line) {
        MyLinkedList<String> fields = new MyLinkedList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else if (c == '"') {
                    inQuotes = false;
                } else {
                    current.append(c);
                }
            } else if (c == '"') {
                inQuotes = true;
            } else if (c == ',') {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }
}
