package com.ceiba.biblioteca.service;

import com.ceiba.biblioteca.model.Prestamo;

import java.util.Optional;

public interface PrestamoService {

    Prestamo save(Prestamo prestamo);

    Optional<Prestamo> findById(Long id);

    Optional<Prestamo> findByIdentificacionUsuario(String identificacionUsuario);

}
