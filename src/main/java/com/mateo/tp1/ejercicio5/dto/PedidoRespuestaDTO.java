package com.mateo.tp1.ejercicio5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRespuestaDTO {
    private Long pedidoId;
    private String cliente;
    private LocalDate fecha;
    private String estado;
    private double totalPedido;
    private List<ProductoPedidoDTO> productos;
}