package com.prestamos.biblioteca.model.service;

import com.prestamos.biblioteca.model.entity.Prestamo;
import java.util.List;

public interface PrestamoService {

    Prestamo save(Prestamo prestamo);

    Prestamo findById(Long id);

    Prestamo findByIdentificacionUsuario(String identificacionUsuario);

    void deleteById(Long id);

    List<Prestamo> findAll();

}
