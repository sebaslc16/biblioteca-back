package com.prestamos.biblioteca.model.entity;

import com.prestamos.biblioteca.common.util.validation.TipoUsuarioValidation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    @NotNull(message = "Ingrese un isbn valido.")
    @Size(min = 1, max = 10, message = "Son permitidos mínimo 1 y máximo 10 dígitos para el isbn.")
    private String isbn;

    @Column(name = "identificacion_usuario", length = 10)
    @NotNull(message = "Ingrese una identificación de usuario valida.")
    @Size(min = 1, max = 10, message = "Son permitidos mínimo 1 y máximo 10 dígitos para la identificación del usuario.")
    private String identificacionUsuario;

    @Column(name = "tipo_usuario", length = 1)
    @NotNull(message = "Ingrese un tipo de usuario valido")
    @TipoUsuarioValidation
    private Integer tipoUsuario;

    @Column(name = "fecha_maxima_devolucion")
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

    public static String calcularFechaMaximaDevolucion(Integer tipoUsuario) {

        LocalDate fechaHoyPrestamo = LocalDate.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        int diasContador = 0;
        switch (tipoUsuario) {
            case (1):
                diasContador = 10;
                break;
            case (2):
                diasContador = 8;
                break;
            case (3):
                diasContador = 7;
                break;
        }

        int diasAgregadosFecha = 0;

        while (diasAgregadosFecha < diasContador) {
            fechaHoyPrestamo = fechaHoyPrestamo.plusDays(1);
            if (!(fechaHoyPrestamo.getDayOfWeek() == DayOfWeek.SATURDAY || fechaHoyPrestamo.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                ++diasAgregadosFecha;
            }
        }
        return fechaHoyPrestamo.format(formatoFecha);

    }
}
