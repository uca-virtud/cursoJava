package es.uca.cursojava.springavanzado.cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El NIF del cliente no puede estar vacío")
    private String nif;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @Email(message = "El email debe ser válido")
    private String email;

    private String notas;

    public Cliente(String nif, String nombre, String email) {
        this.nif = nif;
        this.nombre = nombre;
        this.email = email;
        this.notas = "Cliente nuevo";
    }

    Cliente() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Long getId() {
        return id;
    }

    protected void setId(long l) {
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }
}