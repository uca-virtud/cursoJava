package es.uca.cursojava.springavanzado.cuenta.movimiento;

import es.uca.cursojava.springavanzado.cuenta.Cuenta;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de transacción no puede ser nulo")
    private TipoTransaccion tipo;

    @NotNull(message = "La cantidad no puede ser nula")
    @DecimalMin(value = "0.01", message = "La cantidad debe ser positiva y mayor que cero")
    private BigDecimal cantidad;

    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDateTime fecha = LocalDateTime.now();

    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "La cuenta origen no puede ser nula")
    private Cuenta cuentaOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cuenta cuentaDestino;


    protected Movimiento() {

    }

    public static Movimiento deposito(Cuenta cuenta, BigDecimal cantidad, String descripcion) {
        Movimiento movimiento = new Movimiento();
        movimiento.tipo = TipoTransaccion.DEPOSITO;
        movimiento.cuentaOrigen = cuenta;
        movimiento.cantidad = cantidad;
        movimiento.descripcion = descripcion;
        return movimiento;
    }

    public static Movimiento retiro(Cuenta cuenta, BigDecimal cantidad, String descripcion) {
        Movimiento movimiento = new Movimiento();
        movimiento.tipo = TipoTransaccion.RETIRO;
        movimiento.cuentaOrigen = cuenta;
        movimiento.cantidad = cantidad;
        movimiento.descripcion = descripcion;
        return movimiento;
    }

    public static Movimiento transferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, BigDecimal cantidad, String descripcion) {
        Movimiento movimiento = new Movimiento();
        movimiento.tipo = TipoTransaccion.TRANSFERENCIA;
        movimiento.cuentaOrigen = cuentaOrigen;
        movimiento.cuentaDestino = cuentaDestino;
        movimiento.cantidad = cantidad;
        movimiento.descripcion = descripcion;
        return movimiento;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public Long getId() {
        return id;
    }
}
