package com.mateo.tp1.ejercicio4.service;

import com.mateo.tp1.ejercicio4.dto.ClienteDTO;
import com.mateo.tp1.ejercicio4.entity.Cliente;
import com.mateo.tp1.ejercicio4.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente altaSimple(ClienteDTO dto) {
        Cliente cliente = mapearDesdeDTO(dto);
        return clienteRepository.save(cliente);
    }

    public Cliente altaValidada(ClienteDTO dto) {
        if (clienteRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        Cliente cliente = mapearDesdeDTO(dto);
        return clienteRepository.save(cliente);
    }

    private Cliente mapearDesdeDTO(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setFechaRegistro(LocalDateTime.now());
        return cliente;
    }
}