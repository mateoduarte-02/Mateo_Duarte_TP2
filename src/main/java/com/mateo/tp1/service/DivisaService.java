package com.mateo.tp1.service;

import com.mateo.tp1.dto.ConversionDTO;
import com.mateo.tp1.dto.FrankfurterResponseDTO;
import com.mateo.tp1.exception.ServicioExternoException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.regex.Pattern;

@Service
public class DivisaService {

    private static final Pattern PATRON_MONEDA = Pattern.compile("^[A-Za-z]{3}$");
    private static final String URL_BASE = "https://api.frankfurter.dev/v2";

    private final RestClient restClient;

    public DivisaService() {
        this.restClient = RestClient.builder()
                .baseUrl(URL_BASE)
                .build();
    }

    public ConversionDTO convertir(double monto, String origen, String destino) {

        validarDatos(monto, origen, destino);

        String origenNormalizado = origen.toUpperCase();
        String destinoNormalizado = destino.toUpperCase();

        FrankfurterResponseDTO respuestaExterna;

        try {
            respuestaExterna = restClient.get()
                    .uri("/rate/{origen}/{destino}", origenNormalizado, destinoNormalizado)
                    .retrieve()
                    .body(FrankfurterResponseDTO.class);
        } catch (HttpClientErrorException ex) {
            // La API externa devuelve 422 cuando el código de moneda no existe
            throw new ServicioExternoException(
                    "La API externa no pudo procesar la conversión: código de moneda inexistente");
        } catch (RestClientException ex) {
            throw new ServicioExternoException(
                    "No se pudo obtener la cotización desde el servicio externo: " + ex.getMessage());
        }

        if (respuestaExterna == null) {
            throw new ServicioExternoException("El servicio externo no devolvió información de cotización");
        }

        double tasaCambio = respuestaExterna.getRate();
        double montoConvertido = monto * tasaCambio;

        return new ConversionDTO(
                monto,
                origenNormalizado,
                destinoNormalizado,
                tasaCambio,
                montoConvertido,
                respuestaExterna.getDate()
        );
    }

    private void validarDatos(double monto, String origen, String destino) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que 0");
        }
        if (origen == null || !PATRON_MONEDA.matcher(origen).matches()) {
            throw new IllegalArgumentException("El código de moneda de origen debe tener 3 letras");
        }
        if (destino == null || !PATRON_MONEDA.matcher(destino).matches()) {
            throw new IllegalArgumentException("El código de moneda de destino debe tener 3 letras");
        }
    }
}