package com.mateo.tp1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La categoría no puede estar vacía")
    private String categoria;

    @Positive(message = "El precio debe ser mayor que 0")
    private double precio;

    @Min(value = 0, message = "El stock debe ser mayor o igual que 0")
    private int stock;
}