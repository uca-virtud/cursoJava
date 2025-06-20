package es.uca.cursojava.springavanzado.cuenta.movimiento;

import org.springframework.stereotype.Component;

@Component
public class MovimientoMapper {
    public MovimientoDTO toDTO(Movimiento movimiento) {

        return new MovimientoDTO(
                movimiento.getId(),
                movimiento.getTipo(),
                movimiento.getCantidad(),
                movimiento.getFecha(),
                movimiento.getDescripcion(),
                movimiento.getCuentaOrigen() != null ? movimiento.getCuentaOrigen().getNumero() : null,
                movimiento.getCuentaDestino() != null ? movimiento.getCuentaDestino().getNumero() : null
        );
    }
}
