package no.ntnu.books;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class BookController {
    private static Collection<Book> books;

    public BookController() {
        BookController.books = new ArrayList<>();
        this.initializeData();
    }

    private void initializeData() {
        Book book1 = new Book(0, "Adventure of knights", 1984, 504);
        Book book2 = new Book(1, "Dune", 1965, 577);
        Book book3 = new Book(2, "To Kill a Mockingbird", 1960, 281);
        Book book4 = new Book(3, "1984", 1949, 328);
        Book book5 = new Book(4, "Pride and Prejudice", 1813, 432);

        BookController.books.add(book1);
        BookController.books.add(book2);
        BookController.books.add(book3);
        BookController.books.add(book4);
        BookController.books.add(book5);
    }

    @GetMapping("/books")
    public Collection<Book> getBooks() {
        return BookController.books;
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable int id) {
        Book foundBook = null;
        for (Book book : BookController.books) {
            if (book.getId() == id) {
                foundBook = book;
            }
        }
        return foundBook != null ? new ResponseEntity<>(foundBook, HttpStatus.OK)
                : new ResponseEntity<>(foundBook, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/books")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        if (this.getBook(book.getId()).getStatusCode().equals(HttpStatus.OK)) {
            return new ResponseEntity<>("Error, book already exists", HttpStatus.BAD_REQUEST);
        }

        return BookController.books.add(book) ? new ResponseEntity<>("Book successfully created", HttpStatus.CREATED)
                : new ResponseEntity<>("Error, could not add the book", HttpStatus.BAD_REQUEST);
    }
}
