package com.mateo.tp2.ejercicio2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                                       // Lombok genera getters, setters, toString, equals y hashCode
@NoArgsConstructor                          // genera el constructor vacío: new Producto()
@AllArgsConstructor                         // genera el constructor con todos los campos, en este orden
public class Producto {
    private Long id;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;
}