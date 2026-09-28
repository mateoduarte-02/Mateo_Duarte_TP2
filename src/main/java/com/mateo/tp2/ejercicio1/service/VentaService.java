package com.mateo.tp2.ejercicio1.service;

import com.mateo.tp2.ejercicio1.dto.EstadisticasVentasDTO;
import com.mateo.tp2.ejercicio1.dto.ResultadoDescuentoDTO;
import com.mateo.tp2.ejercicio1.dto.VentaConDescuentoDTO;
import com.mateo.tp2.ejercicio1.dto.VentaConImporteDTO;
import com.mateo.tp2.ejercicio1.dto.VentaDTO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VentaService {

    public EstadisticasVentasDTO calcularEstadisticas(List<VentaDTO> ventas) {

        if (ventas == null || ventas.isEmpty()) {
            throw new IllegalArgumentException("La lista de ventas no puede estar vacía");
        }

        // Convertimos cada VentaDTO a un VentaConImporteDTO (calculando el importe)
        List<VentaConImporteDTO> ventasConImporte = ventas.stream()
                .map(v -> new VentaConImporteDTO(
                        v.getProducto(),
                        v.getCantidad(),
                        v.getPrecioUnitario(),
                        v.getCantidad() * v.getPrecioUnitario()
                ))
                .collect(Collectors.toList());

        double totalFacturado = ventasConImporte.stream()
                .mapToDouble(VentaConImporteDTO::getImporte)
                .sum();

        int cantidadVentas = ventasConImporte.size();

        double ticketPromedio = totalFacturado / cantidadVentas;

        VentaConImporteDTO ventaMayor = ventasConImporte.stream()
                .max(Comparator.comparingDouble(VentaConImporteDTO::getImporte))
                .orElseThrow();

        VentaConImporteDTO ventaMenor = ventasConImporte.stream()
                .min(Comparator.comparingDouble(VentaConImporteDTO::getImporte))
                .orElseThrow();

        // Agrupamos por producto sumando cantidades, y buscamos el máximo
        Map<String, Integer> cantidadPorProducto = ventas.stream()
                .collect(Collectors.groupingBy(
                        VentaDTO::getProducto,
                        Collectors.summingInt(VentaDTO::getCantidad)
                ));

        String productoMasVendido = cantidadPorProducto.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return new EstadisticasVentasDTO(
                totalFacturado,
                cantidadVentas,
                ticketPromedio,
                ventaMayor,
                ventaMenor,
                productoMasVendido
        );
    }

    public ResultadoDescuentoDTO aplicarDescuento(List<VentaDTO> ventas, double porcentaje) {

        if (ventas == null || ventas.isEmpty()) {
            throw new IllegalArgumentException("La lista de ventas no puede estar vacía");
        }

        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        }

        List<VentaConDescuentoDTO> ventasConDescuento = ventas.stream()
                .map(v -> {
                    double montoOriginal = v.getCantidad() * v.getPrecioUnitario();
                    double montoConDescuento = montoOriginal * (1 - porcentaje / 100.0);
                    return new VentaConDescuentoDTO(
                            v.getProducto(),
                            v.getCantidad(),
                            v.getPrecioUnitario(),
                            montoConDescuento
                    );
                })
                .collect(Collectors.toList());

        double totalConDescuento = ventasConDescuento.stream()
                .mapToDouble(VentaConDescuentoDTO::getMontoConDescuento)
                .sum();

        return new ResultadoDescuentoDTO(ventasConDescuento, totalConDescuento);
    }
}