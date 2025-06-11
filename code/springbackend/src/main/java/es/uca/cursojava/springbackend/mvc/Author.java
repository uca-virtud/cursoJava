package es.uca.cursojava.springbackend.mvc;

import jakarta.persistence.Id;

import java.util.UUID;

public class Author {
    @Id
    private UUID id;
    private String nif;
    private String name;
    private String surname;
    private String email;

    public Author() {
    }

    public Author(String nif, String name, String surname, String email) {
        this.id = UUID.randomUUID();
        this.nif = nif;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }
}