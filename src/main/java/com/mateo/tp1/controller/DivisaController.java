package com.mateo.tp1.controller;

import com.mateo.tp1.dto.ApiResponse;
import com.mateo.tp1.dto.ConversionDTO;
import com.mateo.tp1.service.DivisaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/divisas")
@Tag(name = "Divisas", description = "Conversor de divisas usando la API externa Frankfurter")
public class DivisaController {

    private final DivisaService divisaService;

    public DivisaController(DivisaService divisaService) {
        this.divisaService = divisaService;
    }

    @GetMapping("/convertir")
    @Operation(summary = "Convertir un monto entre dos divisas",
            description = "Consulta la API pública Frankfurter para obtener la tasa de cambio actual " +
                    "entre la moneda de origen y la de destino, usando códigos ISO de 3 letras (USD, ARS, EUR, etc.).")
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
}