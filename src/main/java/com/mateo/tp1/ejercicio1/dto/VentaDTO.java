package com.mateo.tp1.ejercicio1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaDTO {

    @NotBlank(message = "El producto no puede estar vacío")
    private String producto;

    @Positive(message = "La cantidad debe ser un número positivo")
    private int cantidad;

    @Positive(message = "El precio unitario debe ser positivo")
    private double precioUnitario;
}