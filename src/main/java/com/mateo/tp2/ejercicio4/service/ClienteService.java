package com.mateo.tp2.ejercicio4.service;

import com.mateo.tp2.ejercicio4.dto.ClienteDTO;
import com.mateo.tp2.ejercicio4.entity.Cliente;
import com.mateo.tp2.ejercicio4.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // Inyección de dependencias:

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente altaSimple(ClienteDTO dto) {      // convierto el DTO en entidad y lo guardo, sin chequeos extra.
        Cliente cliente = mapearDesdeDTO(dto);
        return clienteRepository.save(cliente);      // acá Hibernate genera el INSERT real
    }

    public Cliente altaValidada(ClienteDTO dto) {
        if (clienteRepository.findByEmail(dto.getEmail()).isPresent()) {        // primero reviso si el email ya existe.
            throw new IllegalArgumentException("El email ya está registrado");
        }
        Cliente cliente = mapearDesdeDTO(dto);
        return clienteRepository.save(cliente);
    }

    
    // Está separado para no repetir este código en altaSimple y altaValidada.
    private Cliente mapearDesdeDTO(ClienteDTO dto) {            // arma la entidad a partir del DTO.
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setFechaRegistro(LocalDateTime.now());      // la pone el sistema, no viene en el DTO
        return cliente;
    }
    // El id queda en null a propósito: lo va a generar MySQL con AUTO_INCREMENT recién cuando llamemos a save().
}