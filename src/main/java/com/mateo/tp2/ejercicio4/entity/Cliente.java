package com.mateo.tp2.ejercicio4.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // el id lo genera la base (AUTO_INCREMENT)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)    // no puede repetirse en la tabla
    private String email;

    private String telefono;

    @Column(name = "fecha_registro")        // en Java es fechaRegistro, en la tabla es fecha_registro
    private LocalDateTime fechaRegistro;
}