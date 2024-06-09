package com.ceiba.biblioteca.controller;

import com.ceiba.biblioteca.dto.ResultadoPrestamo;
import com.ceiba.biblioteca.model.Prestamo;
import com.ceiba.biblioteca.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @PostMapping("/prestamo")
    public ResponseEntity<?> savePrestamo(@RequestBody @Valid Prestamo prestamo, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
        LocalDate fechaPrestamo = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        prestamo.setFechaMaximaDevolucion(fechaPrestamo.format(formato));

        Prestamo prestamoGuardado = prestamoService.save(prestamo);
        ResultadoPrestamo resultado = new ResultadoPrestamo(prestamoGuardado.getId(), prestamoGuardado.getFechaMaximaDevolucion());

        return ResponseEntity.ok().body(resultado);

    }

}
