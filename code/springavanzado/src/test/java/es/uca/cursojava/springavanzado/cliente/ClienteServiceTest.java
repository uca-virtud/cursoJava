package es.uca.cursojava.springavanzado.cliente;

import es.uca.cursojava.springavanzado.shared.ResourceNotFoundException;
import es.uca.cursojava.springavanzado.shared.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepo;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;


    @BeforeEach
    void setUp() {
        cliente = new Cliente("111", "Cliente", "cliente@empresa.com");
    }


    @Test
    void shouldObtenerClienteExistente() {
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.obtenerCliente(1L);

        assertEquals(cliente, result);

        verify(clienteRepo).findById(1L);
    }

    @Test
    void shouldNotObtenerClienteNoExistente() {
        when(clienteRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.obtenerCliente(99L));

        verify(clienteRepo).findById(99L);
    }


    @Test
    void shouldRegistrarCliente() {
        when(clienteRepo.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.registrarCliente(cliente);

        assertEquals(cliente, result);

        verify(clienteRepo).save(cliente);
    }


}
