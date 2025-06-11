package es.uca.cursojava.springbackend.hateoas;

import java.util.UUID;

public class Book {
    private UUID id;
    private String isbn;
    private String title;
    private UUID authorId;
    private String genre;
    private Integer year;

    public Book() {
    }

    public Book(String isbn, String title, UUID authorId, String genre, Integer year) {
        this.id = UUID.randomUUID();
        this.isbn = isbn;
        this.title = title;
        this.authorId = authorId;
        this.genre = genre;
        this.year = year;
    }

    // Getters y setters
    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }
}