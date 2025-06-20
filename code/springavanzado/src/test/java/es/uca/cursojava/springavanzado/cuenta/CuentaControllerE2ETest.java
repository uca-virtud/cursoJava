package es.uca.cursojava.springavanzado.cuenta;

import es.uca.cursojava.springavanzado.cliente.Cliente;
import es.uca.cursojava.springavanzado.cliente.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * End‑to‑end simplificado usando {@link TestRestTemplate}, que ya conoce el puerto
 * aleatorio y reduce el boilerplate respecto a un {@code new RestTemplate()} manual.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CuentaControllerE2ETest {

    @Autowired
    private TestRestTemplate rest; // inyectado por Spring Boot

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Crear cuenta para cliente existente devuelve 201 y Location")
    void shouldCrearCuentaParaClienteExistente_E2E() {
        // Given: un cliente en la BD
        Cliente cliente = clienteRepository.save(new Cliente("12345678Z", "Ana Gómez", "ana.gomez@uca.es"));

        // Given: hay un banco aleatorio
        Random random = new Random();
        Long bancoId = random.nextLong(1, 50);

        // Cuerpo JSON sencillo
        String body = """
                { "clienteId": %s,
                  "bancoId": %s
                }
                """.formatted(cliente.getId(), bancoId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("usuario", "usuario"); // autenticación básica

        // When: POST a /cuentas
        ResponseEntity<Void> response = rest.exchange(
                "/api/v1/cuentas", // la URL puede ser relativa
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Void.class);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // pattern de Location: /api/v1/cuentas/{id}
        String expectedLocation = response.getHeaders().getFirst("Location");
        assertEquals(true, expectedLocation.matches("/api/v1/cuentas/\\d+"));
        
        // GET para confirmar que la cuenta existe (pasando auth)
        ResponseEntity<String> followUp = rest.exchange(
                expectedLocation,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);
        assertEquals(HttpStatus.OK, followUp.getStatusCode());
    }

    @Test
    @DisplayName("Crear cuenta para cliente inexistente devuelve 404")
    void shouldNotCrearCuentaParaClienteNoExistente_E2E() {
        String body = """
                { "clienteId": 9999 }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("usuario", "usuario"); // autenticación básica

        ResponseEntity<Void> response = rest.exchange(
                "/api/v1/cuentas",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
