package es.uca.cursojava.springavanzado.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Representa los datos de un cliente")
public record ClienteDTO(
        @Schema(description = "Identificador interno de la cuenta", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Número de identificación fiscal", example = "12345678A")
        @NotBlank(message = "El NIF del cliente no puede estar vacío")
        String nif,

        @Schema(description = "Nombre del cliente", example = "Juan Pérez")
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,

        @Schema(description = "Correo electrónico del cliente", example = "juan.perez@example.com")
        @Email(message = "El email debe ser válido")
        String email
) {
}