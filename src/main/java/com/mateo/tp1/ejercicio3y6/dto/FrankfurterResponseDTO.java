package com.mateo.tp1.ejercicio3y6.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FrankfurterResponseDTO {
    private String date;
    private String base;
    private String quote;
    private double rate;
}