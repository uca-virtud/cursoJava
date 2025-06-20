package es.uca.cursojava.springavanzado.cuenta;

import org.springframework.stereotype.Component;

@Component
public class CuentaMapper {
    public CuentaDTO toDTO(Cuenta cuenta) {

        return new CuentaDTO(
                cuenta.getId(),
                cuenta.getNumero(),
                cuenta.getTitular().getId(),
                cuenta.getSaldo(),
                cuenta.getBanco().getId()
        );
    }
}
