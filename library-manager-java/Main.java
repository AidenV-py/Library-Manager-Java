import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/** Console menu for the library manager. */
public class Main {
    public static void main(String[] args) {
        Library library = new Library(Path.of("library.csv"));
        Scanner in = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Library Manager ===");
            System.out.println("1) List all books");
            System.out.println("2) Add a book");
            System.out.println("3) Check out a book");
            System.out.println("4) Return a book");
            System.out.println("5) Search");
            System.out.println("6) Show overdue books");
            System.out.println("0) Quit");
            System.out.print("Choose: ");

            if (!in.hasNextLine()) break;
            String choice = in.nextLine().trim();

            switch (choice) {
                case "1" -> printAll(library.getAll());
                case "2" -> {
                    System.out.print("Title: ");
                    String title = in.nextLine();
                    System.out.print("Author: ");
                    String author = in.nextLine();
                    Book b = library.addBook(title, author);
                    System.out.println("Added: " + b);
                }
                case "3" -> {
                    int id = readInt(in, "Book ID: ");
                    System.out.print("Borrower name: ");
                    System.out.println(library.checkOut(id, in.nextLine()));
                }
                case "4" -> System.out.println(library.returnBook(readInt(in, "Book ID: ")));
                case "5" -> {
                    System.out.print("Search text: ");
                    printAll(library.search(in.nextLine()));
                }
                case "6" -> printAll(library.getOverdue());
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        System.out.println("Goodbye!");
    }

    private static void printAll(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("(nothing to show)");
            return;
        }
        for (Book b : books) System.out.println(b);
    }

    private static int readInt(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }
}
