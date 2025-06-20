package es.uca.cursojava.springavanzado.cuenta;

import es.uca.cursojava.springavanzado.cuenta.movimiento.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cuentas")
@Tag(name = "Cuentas", description = "Operaciones relacionadas con cuentas")
public class CuentaController {
    private final CuentaService cuentaService;
    private final CuentaMapper cuentaMapper;
    private final MovimientoMapper movimientoMapper;

    public CuentaController(CuentaService cuentaService, CuentaMapper cuentaMapper, MovimientoMapper movimientoMapper) {
        this.cuentaService = cuentaService;
        this.cuentaMapper = cuentaMapper;
        this.movimientoMapper = movimientoMapper;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear cuenta", description = "Crea una nueva cuenta para el cliente especificado")
    public ResponseEntity<CuentaDTO> post(@RequestBody CrearCuentaRequest dto) {
        Cuenta cuenta = cuentaService.crearCuenta(dto.clienteId(), dto.bancoId());
        CuentaDTO result = cuentaMapper.toDTO(cuenta);
        return ResponseEntity.created(URI.create("/api/v1/cuentas/" + result.id())).body(result);
    }

    @GetMapping("/{cuentaId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Obtener cuenta", description = "Retorna los detalles de la cuenta identificada")
    public CuentaDTO get(@PathVariable Long cuentaId) {
        return cuentaMapper.toDTO(cuentaService.obtenerCuenta(cuentaId));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Obtener cuentas por cliente", description = "Retorna la lista de cuentas asociadas a un cliente")
    public List<CuentaDTO> getByCliente(@RequestParam Long clienteId) {
        List<Cuenta> cuentas = cuentaService.obtenerCuentasPorCliente(clienteId);
        return cuentas.stream()
                .map(cuentaMapper::toDTO)
                .toList();
    }

    @GetMapping("/{cuentaId}/movimientos")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Obtener movimientos", description = "Retorna el historial de movimientos de la cuenta")
    public List<MovimientoDTO> getMovimientos(@PathVariable Long cuentaId) {
        List<Movimiento> movimientos = cuentaService.obtenerHistorialMovimientos(cuentaId);
        return movimientos.stream()
                .map(movimientoMapper::toDTO)
                .toList();
    }

    @PostMapping("/{cuentaId}/depositos")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Depositar en cuenta", description = "Realiza un depósito en la cuenta especificada")
    public void depositar(@PathVariable Long cuentaId, @RequestBody DepositoDTO deposito) {
        cuentaService.depositar(cuentaId, deposito.cantidad(), deposito.descripcion());
    }

    @PostMapping("/{cuentaId}/retiros")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retirar de cuenta", description = "Realiza un retiro de la cuenta especificada")
    public void retirar(
            @PathVariable Long cuentaId,
            @RequestBody RetiroDTO retiro) {
        cuentaService.retirar(cuentaId, retiro.cantidad(), retiro.descripcion());
    }

    @PostMapping("/{cuentaId}/transferencias")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Transferir entre cuentas", description = "Realiza una transferencia entre cuentas especificadas")
    public void transferir(@PathVariable Long cuentaId, @RequestBody TransferenciaDTO transferencia) {
        cuentaService.transferir(transferencia.origenId(), transferencia.destinoId(),
                transferencia.cantidad(), transferencia.descripcion());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar cuenta", description = "Elimina la cuenta especificada")
    public void delete(@PathVariable Long id) {
        cuentaService.eliminarCuenta(id);
    }

    public record CrearCuentaRequest(Long clienteId, Long bancoId) {
    }


}
