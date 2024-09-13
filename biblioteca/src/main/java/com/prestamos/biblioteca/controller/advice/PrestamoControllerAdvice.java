package com.prestamos.biblioteca.controller.advice;

import com.prestamos.biblioteca.common.exception.ListaPrestamoException;
import com.prestamos.biblioteca.common.exception.PrestamoException;
import com.prestamos.biblioteca.model.dto.ResultadoErrorPrestamo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NonUniqueResultException;
import java.util.Objects;

@RestControllerAdvice
public class PrestamoControllerAdvice {

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<ResultadoErrorPrestamo> handleMasDeUnPrestamoConsultado(NonUniqueResultException nonUniqueResultException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResultadoErrorPrestamo("Existe más de un prestamo en la consulta! - "+ nonUniqueResultException.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultadoErrorPrestamo> handleValidatorPrestamo(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();

        ResultadoErrorPrestamo resultadoErrorPrestamo = new ResultadoErrorPrestamo(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultadoErrorPrestamo);
    }

    @ExceptionHandler(PrestamoException.class)
    public ResponseEntity<ResultadoErrorPrestamo> handlePrestamo(PrestamoException prestamoException) {
        return ResponseEntity.status(prestamoException.getCodigoError()).body(new ResultadoErrorPrestamo(prestamoException.getMensaje()));
    }

    @ExceptionHandler(ListaPrestamoException.class)
    public ResponseEntity<ResultadoErrorPrestamo> handleListadoVacioPrestamos(ListaPrestamoException listaPrestamoException) {
        return ResponseEntity.status(listaPrestamoException.getCodigoError()).body(new ResultadoErrorPrestamo(listaPrestamoException.getMensaje(),listaPrestamoException.getListaPrestamos()));
    }
}
