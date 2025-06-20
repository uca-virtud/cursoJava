package es.uca.cursojava.springavanzado.cuenta;

import es.uca.cursojava.springavanzado.banco.Banco;
import es.uca.cursojava.springavanzado.banco.BancoService;
import es.uca.cursojava.springavanzado.cliente.Cliente;
import es.uca.cursojava.springavanzado.cliente.ClienteService;
import es.uca.cursojava.springavanzado.cuenta.movimiento.Movimiento;
import es.uca.cursojava.springavanzado.cuenta.movimiento.MovimientoRepository;
import es.uca.cursojava.springavanzado.shared.ResourceNotFoundException;
import es.uca.cursojava.springavanzado.shared.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CuentaService {
    private final MovimientoRepository movimientoRepo;
    private final CuentaRepository cuentaRepo;
    private final ClienteService clienteService;
    private final BancoService bancoService;
    private final NotificationService notificationService;
    Logger logger = LoggerFactory.getLogger(CuentaService.class);

    public CuentaService(CuentaRepository cuentaRepo, MovimientoRepository transaccionRepo, ClienteService clienteService, BancoService bancoService, NotificationService notificationService) {
        this.cuentaRepo = cuentaRepo;
        this.movimientoRepo = transaccionRepo;
        this.clienteService = clienteService;
        this.bancoService = bancoService;
        this.notificationService = notificationService;
    }


    public Cuenta crearCuenta(Long clienteId, Long bancoId) {

        Cliente cliente = clienteService.obtenerCliente(clienteId);

        Banco banco = bancoService.obtenerBanco(bancoId);

        Cuenta cuenta = new Cuenta(UUID.randomUUID().toString(), cliente, BigDecimal.ZERO, banco);

        notificationService.sendNotification(cliente.getEmail(),
                "Cuenta creada",
                "Su cuenta ha sido creada con éxito. Número de cuenta: " + cuenta.getNumero());

        cuentaRepo.save(cuenta);

        return cuenta;

    }


    public Cuenta obtenerCuenta(Long cuentaId) {
        return cuentaRepo.findById(cuentaId)
                .orElseThrow(() -> {
                    logger.error("Cuenta no encontrada para id={}", cuentaId);
                    return new ResourceNotFoundException("Cuenta", cuentaId);
                });
    }


    public List<Cuenta> obtenerCuentasPorCliente(Long clienteId) {
        return cuentaRepo.findByClienteId(clienteId);
    }

    public List<Movimiento> obtenerHistorialMovimientos(Long clienteId) {
        Cuenta cuenta = cuentaRepo.getReferenceById(clienteId);
        return movimientoRepo.findByCuentaOrigenOrCuentaDestino(cuenta, cuenta);
    }


    public void depositar(Long cuentaId, BigDecimal cantidad, String descripcion) {
        logger.info("Iniciando depósito: cuentaId={}, cantidad={}, descripcion={}", cuentaId, cantidad, descripcion);

        Cuenta cuenta = obtenerCuenta(cuentaId);
        logger.debug("Cuenta obtenida: {}", cuenta.getNumero());

        Movimiento movimiento = Movimiento.deposito(cuenta, cantidad, descripcion);
        movimientoRepo.save(movimiento);
        logger.debug("Movimiento de depósito registrado");

        cuenta.incrementarSaldo(cantidad);
        cuentaRepo.save(cuenta);
        logger.info("Saldo incrementado y cuenta actualizada correctamente");
    }


    public void retirar(Long cuentaId, BigDecimal cantidad, String descripcion) {
        Cuenta cuenta = obtenerCuenta(cuentaId);

        cuenta.validarOperacionRetiro(cantidad);

        Movimiento movimiento = Movimiento.retiro(cuenta, cantidad, descripcion);
        movimientoRepo.save(movimiento);

        cuenta.decrementarSaldo(cantidad);
        cuentaRepo.save(cuenta);
    }

    @Transactional
    public void transferir(Long origenId, Long destinoId, BigDecimal cantidad, String descripcion) {
        Cuenta origen = obtenerCuenta(origenId);
        origen.validarOperacionRetiro(cantidad);
        origen.decrementarSaldo(cantidad);
        cuentaRepo.save(origen);

        Cuenta destino = obtenerCuenta(destinoId);
        destino.incrementarSaldo(cantidad);
        cuentaRepo.save(destino);

        Movimiento movimiento = Movimiento.transferencia(origen, destino, cantidad, descripcion);
        movimientoRepo.save(movimiento);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void eliminarCuenta(Long id) {
        Cuenta cuenta = obtenerCuenta(id);
        cuentaRepo.delete(cuenta);
    }
}