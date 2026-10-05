package com.mateo.tp2.ejercicio3y6.service;

import com.mateo.tp2.ejercicio3y6.dto.ConversionDTO;
import com.mateo.tp2.ejercicio3y6.dto.FrankfurterResponseDTO;
import com.mateo.tp2.ejercicio3y6.dto.HistorialConversionDTO;
import com.mateo.tp2.ejercicio3y6.entity.HistorialConversion;
import com.mateo.tp2.exception.ServicioExternoException;
import com.mateo.tp2.ejercicio3y6.repository.HistorialConversionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DivisaService {

    private static final String URL_BASE = "https://api.frankfurter.dev/v2";

    private final RestClient restClient;    // Es la herramienta de Spring para hacer llamadas HTTP a otras APIs
    private final HistorialConversionRepository historialRepository;




    public DivisaService(HistorialConversionRepository historialRepository) {
        this.restClient = RestClient.builder()
                .baseUrl(URL_BASE)
                .build();
        this.historialRepository = historialRepository;
    }




    public ConversionDTO convertir(double monto, String origen, String destino) {

        validarDatos(monto, origen, destino);

        String origenNormalizado = origen.toUpperCase();       //Pasa a mayusculas
        String destinoNormalizado = destino.toUpperCase();

        FrankfurterResponseDTO respuestaExterna;    // Variable donde se guarda la respuesta de Frankfurter (la declaro afuera del try para usarla después)

        try {
            respuestaExterna = restClient.get()
                    .uri("/rate/{origen}/{destino}", origenNormalizado, destinoNormalizado)
                    .retrieve()     // Manda la consulta y trae la respuesta
                    .body(FrankfurterResponseDTO.class);    // Guarda la respuesta en este objeto


        } catch (HttpClientErrorException ex) {
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



    public ConversionDTO consultarYGuardar(double monto, String origen, String destino) {

        ConversionDTO conversion = convertir(monto, origen, destino);

        HistorialConversion registro = new HistorialConversion();
        registro.setMonedaOrigen(conversion.getMonedaOrigen());
        registro.setMonedaDestino(conversion.getMonedaDestino());
        registro.setMonto(conversion.getMontoOriginal());
        registro.setMontoConvertido(conversion.getMontoConvertido());
        registro.setTasa(conversion.getTasaCambio());
        registro.setFechaConsulta(LocalDateTime.now());

        historialRepository.save(registro);

        return conversion;
    }



    public List<HistorialConversionDTO> obtenerHistorial(String origen, String destino) {
        validarCodigoMoneda(origen, "origen");
        validarCodigoMoneda(destino, "destino");

        String origenNormalizado = origen.toUpperCase();
        String destinoNormalizado = destino.toUpperCase();

        List<HistorialConversion> registros = historialRepository
                .findByMonedaOrigenAndMonedaDestinoOrderByFechaConsultaDesc(origenNormalizado, destinoNormalizado);

        return registros.stream()
                .map(r -> new HistorialConversionDTO(r.getFechaConsulta(), r.getTasa()))
                .collect(Collectors.toList());      //Junta los resultados en una lista nueva

        // Entidades guardadas                              Lista devuelta (DTOs)
        // [id=3, USD, ARS, 100, 140000, tasa 1400, 11:30]  ->  [11:30, 1400]
    }




    private void validarDatos(double monto, String origen, String destino) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que 0");
        }
        validarCodigoMoneda(origen, "origen");
        validarCodigoMoneda(destino, "destino");
    }



    // Valida que el código no sea nulo y tenga exactamente 3 caracteres
    private void validarCodigoMoneda(String codigo, String nombreCampo) {
        if (codigo == null || codigo.length() != 3) {
            throw new IllegalArgumentException(
                    "El código de moneda de " + nombreCampo + " debe tener 3 letras");
        }
    }
}