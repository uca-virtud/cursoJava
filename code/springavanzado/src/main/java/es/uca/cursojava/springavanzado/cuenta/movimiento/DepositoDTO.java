package es.uca.cursojava.springavanzado.cuenta.movimiento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

@Schema(description = "Representa un depósito en una cuenta")
public record DepositoDTO(
        @Schema(description = "Cantidad a depositar", example = "100.00")
        @DecimalMin(value = "0.01", message = "La cantidad debe ser positiva y mayor que cero")
        BigDecimal cantidad,
        @Schema(description = "Descripción del depósito", example = "Depósito inicial")
        String descripcion
) {
}