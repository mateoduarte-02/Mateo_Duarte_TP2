package com.mateo.tp2.exception;

public class ServicioExternoException extends RuntimeException {
    public ServicioExternoException(String mensaje) {
        super(mensaje);
    }
}