package com.mateo.tp2.ejercicio3y6.controller;

import com.mateo.tp2.dto.ApiResponse;
import com.mateo.tp2.ejercicio3y6.dto.ConversionDTO;
import com.mateo.tp2.ejercicio3y6.dto.HistorialConversionDTO;
import com.mateo.tp2.ejercicio3y6.service.DivisaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController         // Indica que esta clase recibe peticiones web y devuelve las respuestas en JSON
@RequestMapping("/api/divisas")
@Tag(name = "Divisas", description = "Conversor de divisas usando la API externa Frankfurter, con historial persistido")



public class DivisaController {

    private final DivisaService divisaService;

    public DivisaController(DivisaService divisaService) {
        this.divisaService = divisaService;
    }



    @GetMapping("/convertir")
    @Operation(summary = "Convertir un monto entre dos divisas",
            description = "Consulta la API pública Frankfurter para obtener la tasa de cambio actual entre la moneda de origen y la de destino, usando códigos ISO de 3 letras (USD, ARS, EUR, etc.). No guarda la consulta en el historial.")


    public ResponseEntity<ApiResponse<ConversionDTO>> convertir(
            @Parameter(description = "Monto a convertir, mayor que 0") @RequestParam double monto,
            @Parameter(description = "Código de moneda de origen (3 letras, ej: USD)") @RequestParam String origen,
            @Parameter(description = "Código de moneda de destino (3 letras, ej: ARS)") @RequestParam String destino) {

        ConversionDTO conversion = divisaService.convertir(monto, origen, destino);

        ApiResponse<ConversionDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(), "Conversión realizada con éxito", conversion
        );

        return ResponseEntity.ok(response);
    }


    
    @PostMapping("/consultar")
    @Operation(summary = "Convertir y guardar la consulta en el historial",
            description = "Realiza la conversión como en /convertir, pero además registra la consulta en la tabla historial_conversiones para poder consultarla luego con /historial.")
    
    
        public ResponseEntity<ApiResponse<ConversionDTO>> consultarYGuardar(
            @Parameter(description = "Código de moneda de origen (3 letras, ej: USD)") @RequestParam String origen,
            @Parameter(description = "Código de moneda de destino (3 letras, ej: ARS)") @RequestParam String destino,
            @Parameter(description = "Monto a convertir, mayor que 0") @RequestParam double monto) {

        ConversionDTO conversion = divisaService.consultarYGuardar(monto, origen, destino);

        ApiResponse<ConversionDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(), "Conversión realizada y guardada en el historial", conversion
        );

        return ResponseEntity.ok(response);
    }



    @GetMapping("/historial")
    @Operation(summary = "Consultar el historial de cotizaciones de un par de monedas",
            description = "Devuelve todas las consultas guardadas para el par de monedas indicado, ordenadas de la más reciente a la más antigua.")
    
    
        public ResponseEntity<ApiResponse<List<HistorialConversionDTO>>> obtenerHistorial(
            @Parameter(description = "Código de moneda de origen (3 letras)") @RequestParam String origen,
            @Parameter(description = "Código de moneda de destino (3 letras)") @RequestParam String destino) {

        List<HistorialConversionDTO> historial = divisaService.obtenerHistorial(origen, destino);

        ApiResponse<List<HistorialConversionDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(), "Historial obtenido con éxito", historial
        );

        return ResponseEntity.ok(response);
    }
}