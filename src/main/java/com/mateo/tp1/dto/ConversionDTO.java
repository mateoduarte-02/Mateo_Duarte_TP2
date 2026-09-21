package com.mateo.tp1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionDTO {
    private double montoOriginal;
    private String monedaOrigen;
    private String monedaDestino;
    private double tasaCambio;
    private double montoConvertido;
    private String fecha;
}