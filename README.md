[README.md](https://github.com/user-attachments/files/33318633/README.md)
# Library Manager (Java)

A console application for tracking books, checkouts, and due dates. Data is saved to `library.csv` so it persists between runs.

## Concepts used
- Object-oriented design (`Book` and `Library` classes, encapsulation)
- `ArrayList` collections and searching
- File I/O with `java.nio.file`
- `java.time.LocalDate` for due dates and overdue checks
- Input validation and error handling

## Run it
```bash
javac *.java
java Main
```
Requires Java 17 or newer.

## Features
- Add books, search by title or author
- Check out (14-day loan) and return books
- List overdue books
