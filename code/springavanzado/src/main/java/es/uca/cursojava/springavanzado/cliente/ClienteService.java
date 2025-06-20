package es.uca.cursojava.springavanzado.cliente;

import es.uca.cursojava.springavanzado.shared.ResourceNotFoundException;
import es.uca.cursojava.springavanzado.shared.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class ClienteService {

    private final NotificationService notificationService;

    @Autowired
    private ClienteRepository clienteRepo;

    public ClienteService(ClienteRepository clienteRepo, NotificationService notificationService) {
        this.clienteRepo = clienteRepo;
        this.notificationService = notificationService;
    }

    public List<Cliente> listarClientes() {
        return clienteRepo.findAll();
    }

    public Cliente registrarCliente(Cliente cliente) {

        clienteRepo.save(cliente);

        notificationService.sendNotification(cliente.getEmail(),
                "Usuario registrado",
                "Se ha dado de alta en el sistema. Email: " + cliente.getEmail());

        return cliente;
    }

    public Cliente actualizarCliente(Long id, Cliente cliente) {
        Cliente clienteExistente = obtenerCliente(id);
        clienteExistente.setNombre(cliente.getNombre());
        clienteExistente.setEmail(cliente.getEmail());
        return clienteRepo.save(clienteExistente);

    }

    public Cliente obtenerCliente(Long id) {
        return clienteRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }

    public void eliminarCliente(Long id) {
        Cliente cliente = obtenerCliente(id);
        clienteRepo.delete(cliente);
    }


}