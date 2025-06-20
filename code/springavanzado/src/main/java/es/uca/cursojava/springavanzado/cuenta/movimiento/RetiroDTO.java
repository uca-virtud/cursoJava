package es.uca.cursojava.springavanzado.cuenta.movimiento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

@Schema(description = "Representa un retiro de una cuenta")
public record RetiroDTO(
        @Schema(description = "Cantidad a retirar", example = "50.00")
        @DecimalMin(value = "0.01", message = "La cantidad debe ser positiva y mayor que cero")
        BigDecimal cantidad,
        @Schema(description = "Descripción del retiro", example = "Retiro de efectivo")
        String descripcion
) {
}