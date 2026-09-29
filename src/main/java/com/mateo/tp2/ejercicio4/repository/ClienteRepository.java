package com.mateo.tp2.ejercicio4.repository;

import com.mateo.tp2.ejercicio4.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Spring Data JPA genera automáticamente, en tiempo de ejecución, una clase que implementa todo esto y sabe hablar con la tabla "clientes" de MySQL.
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}
    // Query Method: Spring lee el nombre y arma SELECT * FROM clientes WHERE email = ?
    // Optional porque puede no encontrar ningún cliente con ese email.