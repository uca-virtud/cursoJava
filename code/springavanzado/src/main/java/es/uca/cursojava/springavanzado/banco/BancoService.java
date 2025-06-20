package es.uca.cursojava.springavanzado.banco;

import es.uca.cursojava.springavanzado.banco.ws.BancoMapper;
import es.uca.cursojava.springavanzado.banco.ws.Bank;
import es.uca.cursojava.springavanzado.banco.ws.BankAPIResponse;
import es.uca.cursojava.springavanzado.cuenta.CuentaService;
import es.uca.cursojava.springavanzado.shared.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class BancoService {

    private final BancoRepository bancoRepo;
    private final BancoMapper bancoMapper;
    Logger logger = LoggerFactory.getLogger(CuentaService.class);

    @Value("${ucabank.api.url}")
    private String url;

    public BancoService(BancoRepository bancoRepo, BancoMapper bancoMapper) {
        // Constructor por defecto
        this.bancoRepo = bancoRepo;
        this.bancoMapper = bancoMapper;
    }

    // @Scheduled(cron = "0 0 8 * * *") // Cada día a las 8:00 AM
    @Scheduled(cron = "0 * * * * *") // Cada minuto
    public void scheduledFetching() {
        fetchAll();
    }


    public void fetchAll() {
        logger.info("Descargando datos de bancos desde la URL: {}", url);

        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.getForObject(url, BankAPIResponse.class);
        } catch (Exception e) {
            logger.error("Error al descargar datos de bancos desde la API: {}", e.getMessage());
            return;
        }

        BankAPIResponse response = restTemplate.getForObject(url, BankAPIResponse.class);
        logger.info("Se han recibido {} bancos. Sincronizando base de datos...", response.getBanks().size());
        syncBancos(response.getBanks());
    }

    @CacheEvict(value = "bancosCache", allEntries = true)
    private void syncBancos(List<Bank> banks) {
        for (Bank bank : banks) {

            Optional<Banco> optBanco = bancoRepo.findByExternalId(bank.id());

            if (optBanco.isPresent()) {
                bancoRepo.save(bancoMapper.updateBean(optBanco.get(), bank));
                logger.debug("Banco actualizando correctamente con nombre: {}", optBanco.get().getFullName());
            } else {
                // Crear un nuevo banco
                bancoRepo.save(bancoMapper.toBean(bank));
                logger.debug("Nuevo banco creado con nombre: {}", bank.id());

            }
        }
    }


    @Cacheable(value = "bancosCache", key = "#id")
    public Banco obtenerBanco(Long id) {
        logger.info("Leyendo banco de la base de datos con ID: " + id);

        return bancoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Banco", id));

    }
}
