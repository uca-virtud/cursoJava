package es.uca.cursojava.springavanzado.banco.ws;

import es.uca.cursojava.springavanzado.banco.Banco;
import org.springframework.stereotype.Component;

@Component
public class BancoMapper {
    public Banco toBean(Bank bank) {
        if (bank == null) {
            return null;
        }
        return new Banco(bank.id(), bank.short_name(), bank.full_name(), bank.website());
    }

    public Banco updateBean(Banco existingBanco, Bank bank) {
        existingBanco.setShortName(bank.short_name());
        existingBanco.setFullName(bank.full_name());
        existingBanco.setWebsite(bank.website());
        return existingBanco;
    }
}
