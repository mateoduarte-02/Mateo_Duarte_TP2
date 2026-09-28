package com.mateo.tp2.ejercicio1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaConDescuentoDTO {
    private String producto;
    private int cantidad;
    private double precioUnitario;
    private double montoConDescuento;
}