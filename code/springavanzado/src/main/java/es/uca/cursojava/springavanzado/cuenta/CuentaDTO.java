package es.uca.cursojava.springavanzado.cuenta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Representa una cuenta con su número, saldo y el identificador del cliente")
public record CuentaDTO(
        @Schema(description = "Identificador interno de la cuenta", example = "123", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Número de cuenta", example = "1234567890")
        @NotBlank(message = "El numero de cuenta no puede estar vacío")
        String numero,

        @Schema(description = "Identificador del cliente", example = "1")
        @NotBlank(message = "El titular de la cuenta debe estar informado")
        Long titularId,

        @Schema(description = "Saldo disponible", example = "1000.50")
        @NotNull(message = "El saldo no puede ser nulo")
        BigDecimal saldo,

        @Schema(description = "Identificador del banco", example = "2")
        @NotNull(message = "El banco no puede ser nulo")
        Long bancoId
) {
}