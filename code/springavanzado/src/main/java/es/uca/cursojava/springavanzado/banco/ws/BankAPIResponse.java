package es.uca.cursojava.springavanzado.banco.ws;

import java.util.List;

public class BankAPIResponse {
    private List<Bank> banks;

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }
}