package es.uca.cursojava.springavanzado.shared;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {

    private final String accountNumber;
    private final BigDecimal requiredAmount;

    public InsufficientBalanceException(String accountNumber, BigDecimal requiredAmount) {
        super(String.format("No hay saldo suficiente en la cuenta para retirar %.2f.", requiredAmount));
        this.accountNumber = accountNumber;
        this.requiredAmount = requiredAmount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }


    public BigDecimal getRequiredAmount() {
        return requiredAmount;
    }
}
