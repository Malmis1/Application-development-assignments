package no.ntnu.crudrest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST API controller for book collection.
 */
@RestController
@RequestMapping("/books")
public class BookController {
    private Map<Integer, Book> books;
    private int latestId;

    public BookController() {
        initializeData();
    }

    /**
     * Initialize dummy book data for the collection.
     */
    private void initializeData() {
        latestId = 1;
        books = new HashMap<>();
        addBookToCollection(new Book(-1, "Computer Networks", 2016, 800));
        addBookToCollection(new Book(-1, "12 Rules for Life", 2019, 600));
    }

    private int createNewId() {
        return latestId++;
    }

    /**
     * Get All books.
     * HTTP GET to /books
     *
     * @return List of all books currently stored in the collection
     */
    @GetMapping
    @Operation(summary = "Get all books.", description = "Gets all the books that are stored.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All the book returned in the response body.")
    })
    public Collection<Book> getAll() {
        return books.values();
    }

    /**
     * Get a specific book.
     *
     * @param id ID` of the book to be returned
     * @return Book with the given ID or status 404
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get specific book.", description = "Gets a specific book given the id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The book returned in the response body."),
            @ApiResponse(responseCode = "404", description = "The book with the provided ID is not found, empty body.", content = @Content)
    })
    public ResponseEntity<Book> getOne(
            @Parameter(description = "The ID of the book to get.") @PathVariable Integer id) {
        ResponseEntity<Book> response;
        Book book = findBookById(id);
        if (book != null) {
            response = new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * HTTP POST endpoint for adding a new book.
     *
     * @param book Data of the book to add. ID will be ignored.
     * @return 201 Created on success and the new ID in the response body,
     *         400 Bad request if some data is missing or incorrect
     */
    @PostMapping()
    @Operation(summary = "Adds a book.", description = "Adds a specified book to the library.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The book is added to the library."),
            @ApiResponse(responseCode = "400", description = "The book is invalid and is not added to the library.", content = @Content)
    })
    ResponseEntity<String> add(@Parameter(description = "The book to add.") @RequestBody Book book) {
        ResponseEntity<String> response;

        try {
            int id = addBookToCollection(book);
            response = new ResponseEntity<>("" + id, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * Delete a book from the collection.
     *
     * @param id ID of the book to delete
     * @return 200 OK on success, 404 Not found on error
     */
    @DeleteMapping("/{id}")
    @Operation(hidden = true)
    public ResponseEntity<String> delete(@PathVariable int id) {
        ResponseEntity<String> response;
        if (removeBookFromCollection(id)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Remove a book from the collection.
     *
     * @param id ID of the book to remove
     * @return True when book with that ID existed and was removed, false otherwise.
     */
    private boolean removeBookFromCollection(int id) {
        Book removedBook = books.remove(id);
        return removedBook != null;
    }

    /**
     * Update a book in the repository.
     *
     * @param id   ID of the book to update, from the URL
     * @param book New book data to store, from request body
     * @return 200 OK on success, 400 Bad request on error with error message in the
     *         response body
     */
    @PutMapping("/{id}")
    @Operation(summary = "Updates a book.", description = "Updates a specified book from the library.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The book is updated from the library."),
            @ApiResponse(responseCode = "400", description = "The book is invalid and is not updated from the library.", content = @Content)
    })
    public ResponseEntity<String> update(@Parameter(description = "The book id.") @PathVariable int id,
            @Parameter(description = "The book to update to.") @RequestBody Book book) {
        ResponseEntity<String> response;
        try {
            updateBook(id, book);
            response = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * Search through the book collection, find the book with given ID.
     *
     * @param id Book ID
     * @return Book or null if not found
     */
    private Book findBookById(Integer id) {
        return books.get(id);
    }

    /**
     * Add a new book to the collection. Note: ID will be auto-generated, the
     * original ID will
     * not be used!
     *
     * @param book The book to add
     * @return the ID of the added book
     * @throws IllegalArgumentException When the provided book is not valid
     */
    private int addBookToCollection(Book book) throws IllegalArgumentException {
        if (!book.isValid()) {
            throw new IllegalArgumentException("Book is invalid");
        }
        int id = createNewId();
        books.put(id, new Book(id, book.title(), book.year(), book.numberOfPages()));
        return id;
    }

    /**
     * Try to update a book with given ID. The book.id must match the id.
     *
     * @param id   ID of the book
     * @param book The updated book data
     * @throws IllegalArgumentException If something goes wrong.
     *                                  Error message can be used in HTTP response.
     */
    private void updateBook(int id, Book book) throws IllegalArgumentException {
        Book existingBook = findBookById(id);
        if (existingBook == null) {
            throw new IllegalArgumentException("No book with id " + id + " found");
        }
        if (book == null || !book.isValid()) {
            throw new IllegalArgumentException("Wrong data in request body");
        }
        if (book.id() != id) {
            throw new IllegalArgumentException(
                    "Book ID in the URL does not match the ID in JSON data (response body)");
        }

        books.put(id, book);
    }
}
