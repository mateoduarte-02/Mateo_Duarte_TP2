package com.mateo.tp1.repository;

import com.mateo.tp1.entity.HistorialConversion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialConversionRepository extends JpaRepository<HistorialConversion, Long> {
    List<HistorialConversion> findByMonedaOrigenAndMonedaDestinoOrderByFechaConsultaDesc(
            String monedaOrigen, String monedaDestino);
}