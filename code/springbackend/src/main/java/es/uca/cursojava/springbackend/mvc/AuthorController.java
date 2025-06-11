package es.uca.cursojava.springbackend.mvc;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final Map<UUID, Author> store = new ConcurrentHashMap<>();

    @GetMapping
    public Collection<Author> listAll() {
        return store.values();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Author getById(@PathVariable UUID id) {
        Author author = store.get(id);
        if (author == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No author found with id " + id);
        }
        return author;
    }

    @PostMapping
    public Author create(@RequestBody Author input) {
        validateInput(input);

        Author newAuthor = new Author(
                input.getNif().trim(),
                input.getName().trim(),
                input.getSurname().trim(),
                input.getEmail().trim()
        );

        store.put(newAuthor.getId(), newAuthor);

        return newAuthor;

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Author update(@PathVariable UUID id, @RequestBody Author input) {
        Author existingAuthor = store.get(id);
        if (existingAuthor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No author found with id " + id);
        }
        validateInput(input);

        existingAuthor.setNif(input.getNif().trim());
        existingAuthor.setName(input.getName().trim());
        existingAuthor.setSurname(input.getSurname().trim());
        existingAuthor.setEmail(input.getEmail().trim());

        return existingAuthor;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        Author removed = store.remove(id);
        if (removed == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No author found with id " + id);
        }
    }

    private void validateInput(Author author) {
        if (author.getNif() == null || author.getNif().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NIF cannot be empty");
        }
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be empty");
        }
        if (author.getSurname() == null || author.getSurname().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Surname cannot be empty");
        }
        if (author.getEmail() == null || author.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be empty");
        }
    }


}