package es.uca.cursojava.springavanzado.cliente;

import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    public ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(cliente.getId(), cliente.getNif(), cliente.getNombre(), cliente.getEmail());
    }

    public Cliente toEntity(ClienteDTO clienteDTO) {
        return new Cliente(clienteDTO.nif(), clienteDTO.nombre(), clienteDTO.email());
    }
}
