package com.mateo.tp1.ejercicio5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPedidoDTO {
    private String nombre;
    private String categoria;
    private int cantidad;
    private double subtotal;
}