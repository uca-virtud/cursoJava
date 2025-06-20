package es.uca.cursojava.springavanzado.banco;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BancoRepository extends JpaRepository<Banco, Long> {

    Optional<Banco> findByExternalId(String externalId);

    Optional<Banco> findByShortName(String bbva);
}
