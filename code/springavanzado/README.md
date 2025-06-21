# Proyecto Spring Backend Avanzado

## Funciones principales

- Exponer un servicio web de Clientes
- Exponer un servicio web de Cuentas bancarias

## Aspectos no funcionales

- Generación de documentación OpenAPI
- Seguridad con Spring Security: autenticación básica y control de acceso por URL y método java
- Manejo de excepciones con `@ControllerAdvice`
- Manejo de propiedades de configuración con `@Value`
- Manejo de transacciones con `@Transactional`
- Manejo de pruebas unitarias con `@SpringBootTest` y `@MockBean`
- Manejo de pruebas de integración con `@SpringBootTest` y `@AutoConfigureMockMvc`
- Manejo de cache con `@Cacheable`, `@CachePut` y `@CacheEvict`
- Manejo consumo de servicios web externos con `RestTemplate`
- Manejo de logs con `logback`
- ..

# Base de datos

Configurada para Mysql, pero se puede cambiar a Oracle. Para ello se debe cambiar el dialecto y la URL de conexión en el
archivo `application.properties`.

spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.datasource.url=jdbc:oracle:thin:@//SERVER:1521/DATABASE
