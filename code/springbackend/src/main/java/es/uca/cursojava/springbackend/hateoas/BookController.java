package es.uca.cursojava.springbackend.hateoas;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookModelAssembler assembler;
    private final Map<UUID, Book> store = new ConcurrentHashMap<>();

    public BookController(BookModelAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Book>> listAll() {
        List<EntityModel<Book>> books = store.values().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        // Add links to the collection model

        return CollectionModel.of(books, List.of(
                linkTo(methodOn(BookController.class).listAll()).withSelfRel(),
                linkTo(methodOn(BookController.class).create(null)).withRel("create").withType("POST")));

    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Book> getById(@PathVariable UUID id) {
        Book book = store.get(id);
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No book found with id " + id);
        }
        return assembler.toModel(book);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Book> create(@RequestBody Book input) {
        validateInput(input);

        Book newBook = new Book(
                input.getIsbn().trim(),
                input.getTitle().trim(),
                input.getAuthorId(),
                input.getGenre().trim(),
                input.getYear());

        store.put(newBook.getId(), newBook);

        URI location = URI.create("/api/v1/authors/" + newBook.getId());

        return assembler.toModel(newBook);

    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Book> update(@PathVariable UUID id, @RequestBody Book input) {
        Book existingBook = store.get(id);
        if (existingBook == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No book found with id " + id);
        }
        validateInput(input);

        // Update the existing book with new values
        existingBook.setIsbn(input.getIsbn().trim());
        existingBook.setTitle(input.getTitle().trim());
        existingBook.setAuthorId(input.getAuthorId());
        existingBook.setGenre(input.getGenre());
        existingBook.setYear(input.getYear());

        return assembler.toModel(existingBook);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        Book removed = store.remove(id);
        if (removed == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No book found with id " + id);
        }
    }

    private void validateInput(Book book) {
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ISBN cannot be empty");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be empty");
        }
        if (book.getAuthorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author cannot be empty");
        }
        if (book.getYear() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Year is required");
        }
        if (book.getYear() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Year must be a positive number");
        }

    }


}
