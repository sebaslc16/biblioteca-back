package com.prestamos.biblioteca.common.exception;

import org.springframework.http.HttpStatus;

public class PrestamoException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private HttpStatus codigoError;

    private String mensaje;

    public PrestamoException(HttpStatus codigoError, String mensaje) {
        this.codigoError = codigoError;
        this.mensaje = mensaje;
    }

    public PrestamoException() {
    }

    public HttpStatus getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(HttpStatus codigoError) {
        this.codigoError = codigoError;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
