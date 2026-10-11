import java.time.LocalDate;

/** Represents a single book and its checkout status. */
public class Book {
    private final int id;
    private final String title;
    private final String author;
    private String borrower;      // null when the book is available
    private LocalDate dueDate;    // null when the book is available

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getBorrower() { return borrower; }
    public LocalDate getDueDate() { return dueDate; }

    public boolean isAvailable() { return borrower == null; }

    public boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate);
    }

    public void checkOut(String borrower, LocalDate dueDate) {
        this.borrower = borrower;
        this.dueDate = dueDate;
    }

    public void giveBack() {
        this.borrower = null;
        this.dueDate = null;
    }

    /** Converts this book to one CSV line for saving to disk. */
    public String toCsv() {
        return id + "," + title + "," + author + ","
                + (borrower == null ? "" : borrower) + ","
                + (dueDate == null ? "" : dueDate.toString());
    }

    /** Rebuilds a book from a CSV line created by toCsv(). */
    public static Book fromCsv(String line) {
        String[] p = line.split(",", -1);
        Book b = new Book(Integer.parseInt(p[0]), p[1], p[2]);
        if (!p[3].isEmpty()) {
            b.checkOut(p[3], LocalDate.parse(p[4]));
        }
        return b;
    }

    @Override
    public String toString() {
        String status = isAvailable()
                ? "Available"
                : "Checked out by " + borrower + " (due " + dueDate + (isOverdue() ? ", OVERDUE" : "") + ")";
        return String.format("#%d  %s by %s  [%s]", id, title, author, status);
    }
}
