package es.uca.cursojava.springavanzado.cuenta.movimiento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Representa el detalle de un movimiento de cuenta")
public record MovimientoDTO(

        @Schema(description = "Identificador interno del movimiento", example = "12356", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Tipo de transacción", example = "DEPOSITO")
        @NotNull(message = "El tipo de transacción no puede ser nulo")
        TipoTransaccion tipo,

        @Schema(description = "Cantidad involucrada en la transacción", example = "100.00")
        @DecimalMin(value = "0.01", message = "La cantidad debe ser positiva y mayor que cero")
        BigDecimal cantidad,

        @Schema(description = "Fecha y hora en que se realizó la transacción", example = "2023-10-10T10:15:30")
        @PastOrPresent(message = "La fecha no puede ser futura")
        LocalDateTime fecha,

        @Schema(description = "Descripción de la transacción", example = "Depósito realizado")
        String descripcion,

        @Schema(description = "Número de la cuenta origen", example = "1234567890")
        @NotBlank(message = "El número de cuenta origen no puede estar vacío")
        String numeroCuentaOrigen,

        @Schema(description = "Número de la cuenta destino", example = "0987654321")
        String numeroCuentaDestino
) {
}