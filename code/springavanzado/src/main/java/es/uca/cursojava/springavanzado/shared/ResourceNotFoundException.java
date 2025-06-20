package es.uca.cursojava.springavanzado.shared;

public class ResourceNotFoundException extends RuntimeException {

    private final String resource;
    private final Long id;

    public ResourceNotFoundException(String resource, Long id) {
        super("%s %s no encontrado".formatted(resource, id));
        this.resource = resource;
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public Long getId() {
        return id;
    }
}
