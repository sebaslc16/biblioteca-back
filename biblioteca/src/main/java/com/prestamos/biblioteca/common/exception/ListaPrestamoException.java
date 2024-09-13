package com.prestamos.biblioteca.common.exception;

import com.prestamos.biblioteca.model.entity.Prestamo;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ListaPrestamoException extends PrestamoException{

    List<Prestamo> listaPrestamos;

    public ListaPrestamoException(HttpStatus codigoError, String mensaje, List<Prestamo> listaPrestamos) {
        super(codigoError, mensaje);
        this.listaPrestamos = listaPrestamos;
    }

    public ListaPrestamoException(List<Prestamo> listaPrestamos) {
        this.listaPrestamos = listaPrestamos;
    }

    public List<Prestamo> getListaPrestamos() {
        return listaPrestamos;
    }

    public void setListaPrestamos(List<Prestamo> listaPrestamos) {
        this.listaPrestamos = listaPrestamos;
    }
}
