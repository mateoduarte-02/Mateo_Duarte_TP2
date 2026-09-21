package com.mateo.tp1.dto;

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