package com.mateo.tp2.ejercicio2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    private Long id;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;
}