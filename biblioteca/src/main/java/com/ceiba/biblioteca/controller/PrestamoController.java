package com.ceiba.biblioteca.controller;

import com.ceiba.biblioteca.dto.ResultadoPrestamo;
import com.ceiba.biblioteca.model.Prestamo;
import com.ceiba.biblioteca.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @PostMapping("/prestamo")
    public ResponseEntity<?> savePrestamo(@RequestBody @Valid Prestamo prestamo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
        if (prestamo.getTipoUsuario() == 3) {
            Optional<Prestamo> prestamoInvitado = prestamoService.findByIdentificacionUsuario(prestamo.getIdentificacionUsuario());

            if (prestamoInvitado.isPresent()) {
                Map<String, String> respuesta = new HashMap<>();
                respuesta.put("mensaje", "El usuario con identificación " + prestamo.getIdentificacionUsuario() +
                        " ya tiene un libro prestado por lo cual no se le puede realizar otro préstamo");
                return ResponseEntity.badRequest().body(respuesta);
            }
        }

        prestamo.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamo.getTipoUsuario()));

        Prestamo prestamoGuardado = prestamoService.save(prestamo);
        ResultadoPrestamo resultado = new ResultadoPrestamo(prestamoGuardado.getId(), prestamoGuardado.getFechaMaximaDevolucion());

        return ResponseEntity.ok().body(resultado);

    }

    @GetMapping("/prestamo/{id}")
    public ResponseEntity<?> findByIdPrestamo(@PathVariable Long id) {
        Optional<Prestamo> prestamoConsultado = prestamoService.findById(id);

        if (!prestamoConsultado.isPresent()) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Prestamo con el id " + id + " no existe!");
            return ResponseEntity.badRequest().body(respuesta);
        }
        return ResponseEntity.ok().body(prestamoConsultado.get());
    }

    @GetMapping("/prestamo-identificacion/{idUsuario}")
    public ResponseEntity<?> findByIdentificacionUsuarioPrestamo(@PathVariable String idUsuario) {
        Optional<Prestamo> prestamoConsultadoPorIdentificacion = prestamoService.findByIdentificacionUsuario(idUsuario);

        if (!prestamoConsultadoPorIdentificacion.isPresent()) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje",  "Prestamo con la identificación de usuario " + idUsuario + " no existe!");
            return ResponseEntity.badRequest().body(respuesta);
        }
        return ResponseEntity.ok().body(prestamoConsultadoPorIdentificacion.get());
    }

    @DeleteMapping("/prestamo/{id}")
    public ResponseEntity<?> deletePrestamoById(@PathVariable Long id) {
        Optional<Prestamo> prestamoParaEliminar = prestamoService.findById(id);

        Map<String, String> respuesta = new HashMap<>();
        if(!prestamoParaEliminar.isPresent()) {

            respuesta.put("mensaje",  "No se pudo eliminar el prestamo por que no existe!");
            return ResponseEntity.badRequest().body(respuesta);
        }
        prestamoService.deleteById(id);
        respuesta.put("mensaje",  "Prestamo eliminado correctamente!");
        return ResponseEntity.ok().body(respuesta);
    }

}
