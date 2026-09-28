package com.mateo.tp1.ejercicio1.controller;

import com.mateo.tp1.dto.ApiResponse;
import com.mateo.tp1.ejercicio1.dto.EstadisticasVentasDTO;
import com.mateo.tp1.ejercicio1.dto.ResultadoDescuentoDTO;
import com.mateo.tp1.ejercicio1.dto.VentaDTO;
import com.mateo.tp1.ejercicio1.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Procesamiento de lotes de ventas (en memoria, sin persistencia)")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping("/estadisticas")
    @Operation(summary = "Calcular estadísticas de un lote de ventas",
            description = "Recibe una lista de ventas y devuelve total facturado, ticket promedio, " +
                    "venta mayor/menor y el producto más vendido.")
    public ResponseEntity<ApiResponse<EstadisticasVentasDTO>> obtenerEstadisticas(
            @Valid @RequestBody List<VentaDTO> ventas) {

        EstadisticasVentasDTO estadisticas = ventaService.calcularEstadisticas(ventas);

        ApiResponse<EstadisticasVentasDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Estadísticas calculadas con éxito",
                estadisticas
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/aplicar-descuento")
    @Operation(summary = "Aplicar un descuento a un lote de ventas",
            description = "Recibe una lista de ventas y un porcentaje de descuento (0 a 100), " +
                    "y devuelve cada venta con su monto con descuento aplicado, más el total general.")
    public ResponseEntity<ApiResponse<ResultadoDescuentoDTO>> aplicarDescuento(
            @Valid @RequestBody List<VentaDTO> ventas,
            @Parameter(description = "Porcentaje de descuento a aplicar (entre 0 y 100)")
            @RequestParam double porcentaje) {

        ResultadoDescuentoDTO resultado = ventaService.aplicarDescuento(ventas, porcentaje);

        ApiResponse<ResultadoDescuentoDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Descuento aplicado con éxito",
                resultado
        );

        return ResponseEntity.ok(response);
    }
}