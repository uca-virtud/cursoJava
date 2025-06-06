package es.uca.cursojava.javamoderno;

public enum Status {
    ACTIVE("Activo"),
    INACTIVE("Inactivo"),
    COMPLETED("Completado");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
