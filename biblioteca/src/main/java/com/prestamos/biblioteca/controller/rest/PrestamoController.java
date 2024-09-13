package com.prestamos.biblioteca.controller.rest;

import com.prestamos.biblioteca.model.dto.ResultadoPrestamo;
import com.prestamos.biblioteca.model.entity.Prestamo;
import com.prestamos.biblioteca.model.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @PostMapping("/prestamo")
    public ResponseEntity<ResultadoPrestamo> savePrestamo(@RequestBody @Valid Prestamo prestamo) {
        Prestamo prestamoGuardado = prestamoService.save(prestamo);
        return ResponseEntity.ok().body(new ResultadoPrestamo(prestamoGuardado.getId(), prestamoGuardado.getFechaMaximaDevolucion()));
    }

    @GetMapping("/prestamo/{id}")
    public ResponseEntity<Prestamo> findByIdPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok().body(prestamoService.findById(id));
    }

    @GetMapping("/prestamo-identificacion/{idUsuario}")
    public ResponseEntity<Prestamo> findByIdentificacionUsuarioPrestamo(@PathVariable String idUsuario) {
        return ResponseEntity.ok().body(prestamoService.findByIdentificacionUsuario(idUsuario));
    }

    @DeleteMapping("/prestamo/{id}")
    public ResponseEntity<String> deletePrestamoById(@PathVariable Long id) {
        prestamoService.deleteById(id);
        return ResponseEntity.ok().body("Prestamo eliminado correctamente!");
    }

    @GetMapping("/all")
    public  ResponseEntity<List<Prestamo>> findAllPrestamos() {
        return ResponseEntity.ok().body(prestamoService.findAll());
    }
}
