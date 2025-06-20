package es.uca.cursojava.springavanzado.cuenta;

import es.uca.cursojava.springavanzado.banco.Banco;
import es.uca.cursojava.springavanzado.banco.BancoService;
import es.uca.cursojava.springavanzado.cliente.Cliente;
import es.uca.cursojava.springavanzado.cliente.ClienteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Random;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase // H2 embebida
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BancoService bancoService;

    @Autowired
    private CuentaRepository cuentaRepository;


    @BeforeAll
    public void setup() {
        bancoService.fetchAll();
    }

    @Test
    public void shouldCrearCuentaParaClienteExistente() throws Exception {

        // Given: hay un cliente existente
        Cliente cliente = clienteRepository.save(new Cliente("77777777A", "Juan Pérez", "ivan.ruiz@uca.es"));

        // Given: hay un banco aleatorio
        Random random = new Random();
        Long bancoId = random.nextLong(1, 50);

        // When: se hace la petición para crear una cuenta
        String nuevaCuentaJson = """
                {
                    "clienteId": %s,
                    "bancoId": %s
                }
                """.formatted(cliente.getId(), bancoId);

        mockMvc.perform(post("/api/v1/cuentas")
                        .with(httpBasic("usuario", "usuario")) // Basic Auth credentials
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuevaCuentaJson))
                // Then: se espera que la cuenta se cree correctamente
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", matchesPattern("/api/v1/cuentas/\\d+")));

    }


    @Test
    public void shouldNotCrearCuentaParaClienteNoExistente() throws Exception {
        String nuevaCuentaJson = """
                {
                    "clienteId": 999
                }
                """;

        mockMvc.perform(post("/api/v1/cuentas")
                        .with(httpBasic("usuario", "usuario")) // Basic Auth credentials
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuevaCuentaJson))
                .andExpect(status().isNotFound());
    }


    @Test
    public void shouldTransferirFondosEntreCuentas() throws Exception {
        // Given: hay un banco aleatorio
        Random random = new Random();
        Long bancoId = random.nextLong(1, 50);
        Banco banco = bancoService.obtenerBanco(bancoId);

        // Given: hay dos clientes existentes
        Cliente clienteOrigen = clienteRepository.save(new Cliente("77777777A", "Juan Pérez", "juan@uca.es"));
        Cliente clienteDestino = clienteRepository.save(new Cliente("88888888B", "Ana Gómez", "ana@uca.es"));

        // Given: hay dos cuentas existentes
        Cuenta cuentaOrigen = cuentaRepository.save(new Cuenta("111", clienteOrigen, BigDecimal.valueOf(1000), banco));
        Cuenta cuentaDestino = cuentaRepository.save(new Cuenta("222", clienteDestino, BigDecimal.valueOf(1000), banco));

        BigDecimal cantidadTransferir = BigDecimal.valueOf(200);

        // When: se hace la petición para transferir fondos
        String transferenciaJson = """
                {
                    "origenId": %s,
                    "destinoId": %s,
                    "cantidad": %s
                }
                """.formatted(cuentaOrigen.getId(), cuentaDestino.getId(), cantidadTransferir);

        mockMvc.perform(post("/api/v1/cuentas/" + clienteOrigen.getId() + "/transferencias")
                        .with(httpBasic("usuario", "usuario")) // Basic Auth credentials
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transferenciaJson))
                // Then: se espera que la transferencia se realice correctamente
                .andExpect(status().isOk());

        // Then: verificar saldos
        assertEquals(BigDecimal.valueOf(800).setScale(2), cuentaRepository.findById(cuentaOrigen.getId()).get().getSaldo().setScale(2));
        assertEquals(BigDecimal.valueOf(1200).setScale(2), cuentaRepository.findById(cuentaDestino.getId()).get().getSaldo().setScale(2));

    }


    @Test
    public void shouldTransferirFondosACuentaNoExistente() throws Exception {
        // Given: hay una cliente existente
        Cliente clienteOrigen = clienteRepository.save(new Cliente("77777777A", "Juan Pérez", "ivan.ruiz@uca.es"));

        // Given: hay un banco aleatorio
        Random random = new Random();
        Long bancoId = random.nextLong(1, 50);
        Banco banco = bancoService.obtenerBanco(bancoId);


        // Given: hay una cuenta existente
        Cuenta cuentaOrigen = cuentaRepository.save(new Cuenta("111", clienteOrigen, BigDecimal.valueOf(1000), banco));


        BigDecimal cantidadTransferir = BigDecimal.valueOf(200);
        // When: se hace la petición para transferir fondos a una cuenta no existente
        String transferenciaJson = """
                {
                    "origenId": %s,
                    "destinoId": 999,
                    "cantidad": %s
                }
                """.formatted(cuentaOrigen.getId(), cantidadTransferir);
        mockMvc.perform(post("/api/v1/cuentas/" + clienteOrigen.getId() + "/transferencias")
                        .with(httpBasic("usuario", "usuario")) // Basic Auth credentials
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transferenciaJson))
                // Then: se espera que la transferencia falle
                .andExpect(status().isNotFound());
        // Verificar que el saldo de la cuenta origen no ha cambiado
        assertEquals(BigDecimal.valueOf(1000).setScale(2), cuentaRepository.findById(cuentaOrigen.getId()).get().getSaldo().setScale(2));
    }


}