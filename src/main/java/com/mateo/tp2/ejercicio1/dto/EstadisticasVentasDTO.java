package com.mateo.tp2.ejercicio1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasVentasDTO {
    private double totalFacturado;
    private int cantidadVentas;
    private double ticketPromedio;
    private VentaConImporteDTO ventaMayor;
    private VentaConImporteDTO ventaMenor;
    private String productoMasVendido;
}