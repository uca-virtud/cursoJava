package es.uca.cursojava.springavanzado;

import es.uca.cursojava.springavanzado.banco.BancoService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
@EnableMethodSecurity
@EnableScheduling
public class SpringavanzadoApplication {

    @Autowired
    BancoService bancoService;

    public static void main(String[] args) {
        SpringApplication.run(SpringavanzadoApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "basicAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("API de la Banca Online UCA")
                        .version("v1")
                        .termsOfService("https://www.uca.es/aviso-legal/")
                        .description("API REST para la gestión de clientes, cuentas y movimientos en una aplicación agregadora de servicios de banca online.")
                )
                .addSecurityItem(new SecurityRequirement().addList("securitySchemeName"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")));

    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {

            bancoService.fetchAll();
        };
    }
}
