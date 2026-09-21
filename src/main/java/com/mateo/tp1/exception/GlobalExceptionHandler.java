package com.mateo.tp1.exception;

import com.mateo.tp1.dto.ApiResponse;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - recurso no encontrado (ej: producto/cliente/id inexistente)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> manejarNoEncontrado(ResourceNotFoundException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(), ex.getMessage(), null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 400 - falla @Valid sobre un objeto completo (ej: ProductoDTO, ClienteDTO simples)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> manejarValidacion(MethodArgumentNotValidException ex) {
        List<String> errores = new ArrayList<>();
        Pattern patronIndice = Pattern.compile("^\\[(\\d+)]\\.(.+)$");

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            Matcher matcher = patronIndice.matcher(error.getField());
            String detalle;
            if (matcher.matches()) {
                String posicion = matcher.group(1);
                String campo = matcher.group(2);
                detalle = String.format("Elemento en posición %s, campo '%s': %s",
                        posicion, campo, error.getDefaultMessage());
            } else {
                detalle = String.format("Campo '%s': %s", error.getField(), error.getDefaultMessage());
            }
            errores.add(detalle);
        }

        ApiResponse<List<String>> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(), "Error de validación en los datos enviados", errores
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 400 - falla validación sobre @RequestParam/@PathVariable o List<DTO> con @Valid como parámetro directo
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<List<String>>> manejarParametrosInvalidos(HandlerMethodValidationException ex) {
        List<String> errores = new ArrayList<>();

        // Errores anidados (ej: cada elemento inválido de List<VentaDTO> con @Valid)
        for (ParameterErrors parametroErrores : ex.getBeanResults()) {
            Integer indice = parametroErrores.getContainerIndex();
            String prefijoPosicion = (indice != null) ? "Elemento en posición " + indice + ", " : "";
            for (FieldError error : parametroErrores.getFieldErrors()) {
                errores.add(prefijoPosicion + "campo '" + error.getField() + "': " + error.getDefaultMessage());
            }
        }

        // Errores directos (ej: constraints sobre un @RequestParam/@PathVariable suelto)
        for (ParameterValidationResult resultado : ex.getValueResults()) {
            Integer indice = resultado.getContainerIndex();
            String prefijoPosicion = (indice != null) ? "Elemento en posición " + indice + ", " : "";
            String mensaje = resultado.getResolvableErrors().stream()
                    .map(MessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            errores.add(prefijoPosicion + mensaje);
        }

        ApiResponse<List<String>> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(), "Error de validación en los datos enviados", errores
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 400 - errores de validación agrupados por campo (usado por ClienteDTO en /api/clientes/validado)
    @ExceptionHandler(ClienteValidacionException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> manejarValidacionCliente(ClienteValidacionException ex) {
        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(), "Error de validación", ex.getErrores()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 400 - JSON malformado en el body (llaves faltantes, comas de más, sintaxis inválida)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> manejarJsonMalformado(HttpMessageNotReadableException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "El cuerpo de la solicitud tiene un formato JSON inválido",
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 400 - validaciones manuales (ej: porcentaje fuera de rango, email ya registrado)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> manejarArgumentoInvalido(IllegalArgumentException ex) {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 502 - falla la comunicación con un servicio externo (Ejercicio 3/6 - Frankfurter)
    @ExceptionHandler(ServicioExternoException.class)
    public ResponseEntity<ApiResponse<Void>> manejarServicioExterno(ServicioExternoException ex) {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.BAD_GATEWAY.value(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    // 500 - cualquier otro error no controlado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> manejarErrorGenerico(Exception ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrió un error inesperado: " + ex.getMessage(), null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}