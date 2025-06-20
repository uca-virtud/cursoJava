package es.uca.cursojava.springavanzado.cuenta.movimiento;

import es.uca.cursojava.springavanzado.cuenta.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaOrigenOrCuentaDestino(Cuenta cuentaOrigen, Cuenta cuentaDestino);
}