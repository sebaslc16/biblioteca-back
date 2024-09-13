package com.prestamos.biblioteca.model.repository;

import com.prestamos.biblioteca.model.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    Optional<Prestamo> findByIsbn(String isbn);

    Optional<Prestamo> findByIdentificacionUsuario(String identificacionUsuario);

}
