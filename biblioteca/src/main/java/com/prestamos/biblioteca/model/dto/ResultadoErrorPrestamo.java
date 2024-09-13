package com.prestamos.biblioteca.model.dto;

import com.prestamos.biblioteca.model.entity.Prestamo;

import java.util.List;

public class ResultadoErrorPrestamo {

    private String mensaje;

    private List<Prestamo> prestamos;

    public ResultadoErrorPrestamo(String mensaje) {
        this.mensaje = mensaje;
    }

    public ResultadoErrorPrestamo(String mensaje, List<Prestamo> prestamos) {
        this.mensaje = mensaje;
        this.prestamos = prestamos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }
}
