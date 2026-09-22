package com.mateo.tp1.controller;

import com.mateo.tp1.dto.ApiResponse;
import com.mateo.tp1.dto.PedidoRespuestaDTO;
import com.mateo.tp1.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Historial de pedidos con filtros combinables")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar pedidos con filtros opcionales combinables",
            description = "Filtra por cliente, categoría, rango de fechas y/o estado. Todos los parámetros son opcionales.")
    public ResponseEntity<ApiResponse<List<PedidoRespuestaDTO>>> buscar(
            @Parameter(description = "Id del cliente") @RequestParam(required = false) Long clienteId,
            @Parameter(description = "Nombre de la categoría") @RequestParam(required = false) String categoria,
            @Parameter(description = "Fecha inicial (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @Parameter(description = "Fecha final (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @Parameter(description = "Estado: PENDIENTE, ENVIADO, ENTREGADO o CANCELADO")
            @RequestParam(required = false) String estado) {

        List<PedidoRespuestaDTO> pedidos = pedidoService.buscarConFiltros(
                clienteId, categoria, fechaDesde, fechaHasta, estado);

        ApiResponse<List<PedidoRespuestaDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(), "Consulta realizada correctamente", pedidos
        );

        return ResponseEntity.ok(response);
    }
}