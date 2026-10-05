package com.mateo.tp2.ejercicio3y6.repository;

import com.mateo.tp2.ejercicio3y6.entity.HistorialConversion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialConversionRepository extends JpaRepository<HistorialConversion, Long> {
    // Busca por moneda origen y moneda destino, ordenado por fecha de consulta, descendente.
    List<HistorialConversion> findByMonedaOrigenAndMonedaDestinoOrderByFechaConsultaDesc(
            String monedaOrigen, String monedaDestino);
}


/* Spring convierte findByMonedaOrigenAndMonedaDestinoOrderByFechaConsultaDesc en este SQL:
   SELECT * FROM historial_conversiones
   WHERE moneda_origen = 'USD' AND moneda_destino = 'ARS'
   ORDER BY fecha_consulta DESC */