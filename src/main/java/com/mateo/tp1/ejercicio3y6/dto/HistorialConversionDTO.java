package com.mateo.tp1.ejercicio3y6.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialConversionDTO {
    private LocalDateTime fecha;
    private double tasaCambio;
}