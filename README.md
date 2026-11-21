# CIRCA: Library Book Reservation and Borrowing Management System

## Description

CIRCA is a Java desktop (Swing) application for managing a school library. Students can search the book catalog, borrow and return books, reserve books that are currently unavailable, and view their borrowing history. Librarians can manage the book inventory, student accounts, borrowing records and returns, and can undo the most recent transaction if a mistake was made.

The project addresses two problems: slowdowns when the amount of library data grows, and the lack of a way to recover from transactional errors. It does this by using suitable data structures and algorithms instead of simple lists, and by keeping a stack of recent actions that can be undone.

**Main features**

- Login and registration (passwords are stored as SHA-256 hashes)
- Advanced book search by ID, title, author, ISBN or category, with sorting and paging
- Borrowing and returning with due dates and overdue status
- First-in-first-out reservation queue for books with no available copies
- Automatic update of book availability
- Borrowing history that can be browsed forward and backward
- Librarian dashboard: manage books, students, borrowing records and returns, plus undo
- Data saved to plain text (.txt) files and reloaded on the next start

**Data structures and algorithms used**

| Data structure / algorithm | Where it is used |
|---|---|
| Array | Display and lookup of records in tables and pages |
| Linked list | Books, users, borrow records and transactions |
| Doubly linked list | A student's transaction history (previous / next) |
| Queue | Reservation queue of each book (FIFO) |
| Hash table | Fast lookup of books, users, records by ID |
| Stack | Recent actions for undo (LIFO) |
| Merge sort | Sorting books, records and transactions |
| Binary search tree | Books ordered by title |

## Team Members

| Name | Role |
|---|---|
<<<<<<< HEAD
| Red Liegh Gonzales | Developer |
| Peter Padillo | Project Lead, Developer |
| Carenne Ryssa Wong | Documentation, UI Developer |
| Isabel Victoria Francisco | UI/ UX Developer |
=======
| [Red Liegh Gonzales] | [Developer] |
| [Peter Padillo] | [Project Lead, Developer] |
| [Carenne Ryssa Wong] | [Documentation, UI Developer] |
| [Isabel Victoria Francisco] | [Documentation, UI Developer] |
>>>>>>> 1a93c7cd73cd4ada2f0ab6542ebd5310c69289ca

## Requirements


## How to Run

1. Clone or download this repository and open a terminal in the project folder.
2. Compile the source code.

   macOS / Linux:
   ```
   mkdir -p out
   javac -d out $(find src -name "*.java")
   ```

   Windows (Command Prompt):
   ```
   mkdir out
   dir /s /b src\*.java > sources.txt
   javac -d out @sources.txt
   ```

3. Start the program:
   ```
   java -cp out circa.Main
   ```

Alternatively, open the project in an IDE (NetBeans, IntelliJ IDEA, Eclipse), mark `src` as the source folder, and run `circa.Main`.

### Sample accounts

| Role | ID | Password |
|---|---|---|
| Librarian | LIB001 | admin123 |
| Student | STU001 | pass1 |
| Student | STU002 | pass2 |
| Student | STU003 | pass3 |

New students can also register from the login screen.

### Saved data

On the first run the program creates a `data/` folder next to where it is started, filled with sample books and accounts. It contains `users.txt`, `books.txt`, `borrow_records.txt` and `transactions.txt`, and is updated after every change. To reset the system to the sample data, close the program and delete the `data/` folder. The undo history only lasts for the current session.

<<<<<<< HEAD
### Logo

To show the CIRCA logo in the login screen and the window headers, save it as `images/logo.png` in the project folder (next to `src`) and run the program from that folder. If the file is missing, a placeholder box is shown instead.

=======
>>>>>>> 1a93c7cd73cd4ada2f0ab6542ebd5310c69289ca
## Project Structure

```
src/circa/
  view/         Swing windows and screens
  controller/   Connects the screens to the services
  service/      Business rules (borrowing, books, users)
  repository/   Storage of records in memory using the data structures
  storage/      Text file saving and loading
  model/        Book, Student, Librarian, BorrowRecord, Transaction
  undo/         Undoable actions and the undo manager
  structures/   Linked lists, queue, stack, hash table, BST, merge sort
<<<<<<< HEAD
images/         Put the logo here as logo.png
=======
>>>>>>> 1a93c7cd73cd4ada2f0ab6542ebd5310c69289ca
uml/            UML class diagram (PNG, PDF, SVG)
```

## UML Diagram

The class diagram is in the `uml` folder: `class-diagram.png` (also available as `.pdf` and `.svg`).
<<<<<<< HEAD
=======
"# DSA_FinalProject" 
>>>>>>> 1a93c7cd73cd4ada2f0ab6542ebd5310c69289ca
