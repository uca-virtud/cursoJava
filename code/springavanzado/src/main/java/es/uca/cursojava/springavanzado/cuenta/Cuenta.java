package es.uca.cursojava.springavanzado.cuenta;

import es.uca.cursojava.springavanzado.banco.Banco;
import es.uca.cursojava.springavanzado.cliente.Cliente;
import es.uca.cursojava.springavanzado.shared.InsufficientBalanceException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El numero de cuenta no puede estar vacío")
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "El titular de la cuenta no puede ser nulo")
    private Cliente titular;

    @NotNull(message = "El saldo no puede ser nulo")
    private BigDecimal saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "El banco no puede ser nulo")
    private Banco banco;

    public Cuenta(String numero, Cliente titular, BigDecimal cantidadInicial, Banco banco) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = cantidadInicial;
        this.banco = banco;
    }

    Cuenta() {
        // Constructor por defecto para JPA
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public Cliente getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void incrementarSaldo(BigDecimal cantidad) {
        if (cantidad.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cantidad a incrementar no puede ser negativa");
        }
        this.saldo = this.saldo.add(cantidad);
    }

    public void decrementarSaldo(BigDecimal cantidad) {
        if (cantidad.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cantidad a decrementar no puede ser negativa");
        }
        if (this.saldo.compareTo(cantidad) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar la operación");
        }
        this.saldo = this.saldo.subtract(cantidad);
    }


    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public void validarOperacionRetiro(BigDecimal cantidad) {
        if (cantidad.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cantidad a retirar no puede ser negativa");
        }
        if (this.saldo.compareTo(cantidad) < 0) {
            throw new InsufficientBalanceException(this.numero, cantidad);
        }
    }
}