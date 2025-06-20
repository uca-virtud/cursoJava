package es.uca.cursojava.springavanzado.shared;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.OffsetDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // Se podria simplificar este codigo no mapeando excepciones comunes


    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleNotFound(ResourceNotFoundException ex,
                                 HttpServletRequest request) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage());                       // “Account 42 no encontrado”

        pd.setTitle("Recurso no encontrado");
        pd.setType(URI.create("https://api.ejemplo.com/errors/not-found"));
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("resource", ex.getResource());
        pd.setProperty("id", ex.getId());
        pd.setProperty("timestamp", OffsetDateTime.now());

        return pd;      // Spring lo renderiza como JSON RFC 7807
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    ProblemDetail handleSaldoInsuficiente(InsufficientBalanceException ex,
                                          HttpServletRequest request) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage());  // “Saldo insuficiente para realizar la operación”

        pd.setTitle("Saldo insuficiente");
        pd.setType(URI.create("https://api.ejemplo.com/errors/saldo-insuficiente"));
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("numeroCuenta", ex.getAccountNumber());
        pd.setProperty("cantidadRequerida", ex.getRequiredAmount());
        pd.setProperty("timestamp", OffsetDateTime.now());

        return pd;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleIllegalArgument(IllegalArgumentException ex,
                                        HttpServletRequest request) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage());  // Mensaje de error específico

        pd.setTitle("Argumento inválido");
        pd.setType(URI.create("https://api.ejemplo.com/errors/argumento-invalido"));
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("timestamp", OffsetDateTime.now());

        return pd;
    }

    /*
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidationError(MethodArgumentNotValidException ex,
                                        HttpServletRequest request) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Errores de validación en los datos proporcionados");

        pd.setTitle("Error de validación");
        pd.setType(URI.create("https://api.ejemplo.com/errors/validacion"));
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("timestamp", OffsetDateTime.now());
        pd.setProperty("errors", ex.getBindingResult().getFieldErrors());

        return pd;
    }*/

}
