package es.uca.cursojava.springavanzado.cuenta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    @Query("SELECT c FROM Cuenta c WHERE c.titular.id = :clienteId")
    List<Cuenta> findByClienteId(Long clienteId);
}