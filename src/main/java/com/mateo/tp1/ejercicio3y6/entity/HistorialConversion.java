package com.mateo.tp1.ejercicio3y6.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_conversiones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialConversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "moneda_origen")
    private String monedaOrigen;

    @Column(name = "moneda_destino")
    private String monedaDestino;

    private double monto;

    @Column(name = "monto_convertido")
    private double montoConvertido;

    private double tasa;

    @Column(name = "fecha_consulta")
    private LocalDateTime fechaConsulta;
}