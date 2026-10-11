import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Manages a collection of books and saves them to a CSV file. */
public class Library {
    private static final int LOAN_DAYS = 14;

    private final List<Book> books = new ArrayList<>();
    private final Path file;
    private int nextId = 1;

    public Library(Path file) {
        this.file = file;
        load();
    }

    public Book addBook(String title, String author) {
        // Commas would break the CSV format, so replace them.
        Book b = new Book(nextId++, title.replace(",", " "), author.replace(",", " "));
        books.add(b);
        save();
        return b;
    }

    public Book find(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    public String checkOut(int id, String borrower) {
        Book b = find(id);
        if (b == null) return "No book with that ID.";
        if (!b.isAvailable()) return "That book is already checked out.";
        b.checkOut(borrower.replace(",", " "), LocalDate.now().plusDays(LOAN_DAYS));
        save();
        return "Checked out. Due " + b.getDueDate() + ".";
    }

    public String returnBook(int id) {
        Book b = find(id);
        if (b == null) return "No book with that ID.";
        if (b.isAvailable()) return "That book is not checked out.";
        b.giveBack();
        save();
        return "Book returned.";
    }

    public List<Book> getAll() { return books; }

    public List<Book> getOverdue() {
        List<Book> overdue = new ArrayList<>();
        for (Book b : books) {
            if (b.isOverdue()) overdue.add(b);
        }
        return overdue;
    }

    public List<Book> search(String text) {
        String t = text.toLowerCase();
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(t) || b.getAuthor().toLowerCase().contains(t)) {
                results.add(b);
            }
        }
        return results;
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Book b : books) lines.add(b.toCsv());
        try {
            Files.write(file, lines);
        } catch (IOException e) {
            System.out.println("Warning: could not save data: " + e.getMessage());
        }
    }

    private void load() {
        if (!Files.exists(file)) return;
        try {
            for (String line : Files.readAllLines(file)) {
                if (line.isBlank()) continue;
                Book b = Book.fromCsv(line);
                books.add(b);
                nextId = Math.max(nextId, b.getId() + 1);
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Warning: could not read saved data: " + e.getMessage());
        }
    }
}
