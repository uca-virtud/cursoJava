package es.uca.cursojava.springavanzado.cliente;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase // H2 embebida
public class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;


    @Test
    public void shouldCrearCliente() throws Exception {

        // When: se hace la petición para crear un cliente
        String nuevoClienteJson = """
                {
                    "nif": "55555555Z",
                    "nombre": "Mariano Nano",
                    "email": "mariano@@uca.es"
                }
                """;

        mockMvc.perform(post("/api/v1/clientes")
                        .with(httpBasic("admin", "admin")) // Basic Auth credentials
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuevoClienteJson))
                // Then: se espera que la cuenta se cree correctamente envindo la ubicacion a "/api/v1/clientes/{idGenerado}
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", matchesPattern("/api/v1/clientes/\\d+")));

    }


    @Test
    public void shouldNotCrearClienteSinNIF() throws Exception {

        // When: se hace la petición para crear un cliente sin NIF
        String nuevoClienteJson = """
                {
                    "nombre": "Hector Tilla",
                    "email": "hector@uca.es"
                    }
                """;

        mockMvc.perform(post("/api/v1/clientes")
                        .with(httpBasic("admin", "admin")) // Basic Auth credentials
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuevoClienteJson))
                // Then: se espera que la petición falle con un error 400 Bad Request
                .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldNotCrearClienteSinAuth() throws Exception {
        // When: se hace la petición para crear un cliente sin autenticación
        String nuevoClienteJson = """
                {
                    "nif": "55555555Z",
                    "nombre": "Mariano Nano",
                    "email": "mariano@uca.es"
                }
                """;

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuevoClienteJson))
                // Then: se espera que la petición falle con un error 401 Unauthorized
                .andExpect(status().isUnauthorized());
    }


}