package com.mateo.tp2.ejercicio1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoDescuentoDTO {
    private List<VentaConDescuentoDTO> ventas;
    private double totalConDescuento;
}