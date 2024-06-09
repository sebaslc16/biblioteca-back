package com.ceiba.biblioteca.model;

import com.ceiba.biblioteca.util.TipoUsuarioValidation;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    @Size(min = 1, max = 10, message = "Son permitidos mínimo 1 y máximo 10 dígitos para el isbn.")
    private String isbn;

    @Column(name = "identificacion_usuario",length = 10)
    @Size(min = 1, max = 10, message = "Son permitidos mínimo 1 y máximo 10 dígitos para la identificación del usuario.")
    private String identificacionUsuario;

    @Column(name = "tipo_usuario", length = 1)
    @TipoUsuarioValidation
    private Integer tipoUsuario;

    @Column(name = "fecha_maxima_devolucion")
    @Transient
    private String fechaMaximaDevolucion;

    public Prestamo() {
    }

    public Prestamo(String isbn, String identificacionUsuario, Integer tipoUsuario) {
        this.isbn = isbn;
        this.identificacionUsuario = identificacionUsuario;
        this.tipoUsuario = tipoUsuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIdentificacionUsuario() {
        return identificacionUsuario;
    }

    public void setIdentificacionUsuario(String identificacionUsuario) {
        this.identificacionUsuario = identificacionUsuario;
    }

    public Integer getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Integer tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getFechaMaximaDevolucion() {
        return fechaMaximaDevolucion;
    }

    public void setFechaMaximaDevolucion(String fechaMaximaDevolucion) {
        this.fechaMaximaDevolucion = fechaMaximaDevolucion;
    }
}
