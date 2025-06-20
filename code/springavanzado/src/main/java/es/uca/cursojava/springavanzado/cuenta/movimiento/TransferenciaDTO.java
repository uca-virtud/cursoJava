package es.uca.cursojava.springavanzado.cuenta.movimiento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Representa una transferencia entre cuentas")
public record TransferenciaDTO(
        @Schema(description = "Identificador de la cuenta origen", example = "1")
        @NotNull(message = "El origenId no puede ser nulo")
        Long origenId,

        @Schema(description = "Identificador de la cuenta destino", example = "2")
        @NotNull(message = "El destinoId no puede ser nulo")
        Long destinoId,

        @Schema(description = "Cantidad a transferir", example = "250.00")
        @DecimalMin(value = "0.01", message = "La cantidad debe ser positiva y mayor que cero")
        BigDecimal cantidad,

        @Schema(description = "Descripción de la transferencia", example = "Transferencia para pago")
        String descripcion
) {
}