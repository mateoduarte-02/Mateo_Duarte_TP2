package com.mateo.tp2.ejercicio4.controller;

import com.mateo.tp2.dto.ApiResponse;
import com.mateo.tp2.ejercicio4.dto.ClienteDTO;
import com.mateo.tp2.ejercicio4.entity.Cliente;
import com.mateo.tp2.exception.ClienteValidacionException;
import com.mateo.tp2.ejercicio4.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Alta de clientes, persistidos en base de datos MySQL")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @Operation(summary = "Alta simple de un cliente",
            description = "Inserta un cliente en la base de datos sin validaciones adicionales.")
    public ResponseEntity<ApiResponse<Cliente>> altaSimple(@RequestBody ClienteDTO clienteDTO) {
        Cliente clienteCreado = clienteService.altaSimple(clienteDTO);
        ApiResponse<Cliente> response = new ApiResponse<>(
                HttpStatus.CREATED.value(), "Cliente creado con éxito", clienteCreado
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/validado")
    @Operation(summary = "Alta de un cliente con validaciones",
            description = "Valida nombre, apellido, email (formato y unicidad) y teléfono antes de insertar.")
    public ResponseEntity<ApiResponse<Cliente>> altaValidada(
            @Valid @RequestBody ClienteDTO clienteDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errores.put(error.getField(), error.getDefaultMessage());
            }
            throw new ClienteValidacionException(errores);
        }

        Cliente clienteCreado = clienteService.altaValidada(clienteDTO);

        ApiResponse<Cliente> response = new ApiResponse<>(
                HttpStatus.CREATED.value(), "Cliente creado con éxito", clienteCreado
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}