package com.mateo.tp1.exception;

import java.util.Map;

public class ClienteValidacionException extends RuntimeException {

    private final Map<String, String> errores;

    public ClienteValidacionException(Map<String, String> errores) {
        super("Error de validación");
        this.errores = errores;
    }

    public Map<String, String> getErrores() {
        return errores;
    }
}