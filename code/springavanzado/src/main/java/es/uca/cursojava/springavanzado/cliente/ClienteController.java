package es.uca.cursojava.springavanzado.cliente;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Operaciones relacionadas con clientes")
public class ClienteController {
    private final ClienteService clienteService;
    private final ClienteMapper mapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.mapper = clienteMapper;
    }

    @PostMapping
    @Operation(summary = "Registrar cliente", description = "Registra un nuevo cliente a partir de los datos proporcionados")
    public ResponseEntity<ClienteDTO> post(@RequestBody @Valid ClienteDTO dto) {
        Cliente cliente = clienteService.registrarCliente(mapper.toEntity(dto));
        ClienteDTO result = mapper.toDTO(cliente);
        return ResponseEntity.created(URI.create("/api/v1/clientes/" + result.id())).body(result);
    }

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Retorna la lista de todos los clientes registrados")
    @ResponseStatus(HttpStatus.OK)
    public List<ClienteDTO> getAll() {
        return clienteService.listarClientes().stream()
                .map(cliente -> mapper.toDTO(cliente))
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente", description = "Retorna los detalles de un cliente específico identificado por el ID")
    @ResponseStatus(HttpStatus.OK)
    public ClienteDTO get(@PathVariable Long id) {
        return mapper.toDTO(clienteService.obtenerCliente(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente identificado por el ID")
    @ResponseStatus(HttpStatus.OK)
    public ClienteDTO patch(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        Cliente cliente = clienteService.actualizarCliente(id, mapper.toEntity(dto));
        return mapper.toDTO(cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente específico identificado por el ID")
    public void delete(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }
}