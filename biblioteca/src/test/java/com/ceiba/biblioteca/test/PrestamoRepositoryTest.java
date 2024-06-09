package com.ceiba.biblioteca.test;

import com.ceiba.biblioteca.model.Prestamo;
import com.ceiba.biblioteca.repository.PrestamoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("test_integracion_jpa")
@DataJpaTest
public class PrestamoRepositoryTest {

    @Autowired
    PrestamoRepository prestamoRepository;

    @Test
    @Tag("repository")
    @DisplayName("Test del metodo findById")
    @Order(1)
    void testFindByIdPrestado() {
        Optional<Prestamo> prestamoNoExistente = prestamoRepository.findById(1L);

        assertFalse(prestamoNoExistente.isPresent());

        Prestamo prestamoNuevo = new Prestamo("AI9942", "Z940402", 2);
        LocalDate fechaPrestamo = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        prestamoNuevo.setFechaMaximaDevolucion(fechaPrestamo.format(formato));

        Prestamo prestamoGuardado = prestamoRepository.save(prestamoNuevo);
        System.out.println(prestamoGuardado.getId());

        Optional<Prestamo> prestamo1 = prestamoRepository.findById(1L);
        assertTrue(prestamo1.isPresent());
    }

    @Test
    @Tag("repository")
    @DisplayName("Test del metodo save")
    @Order(2)
    void testSavePrestamo() {
        Prestamo prestamoNuevo = new Prestamo("i9383", "nicjef3", 1);
        LocalDate fechaPrestamo = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        prestamoNuevo.setFechaMaximaDevolucion(fechaPrestamo.format(formato));

        Prestamo prestamoGuardado = prestamoRepository.save(prestamoNuevo);

        assertNotNull(prestamoGuardado);
        assertEquals("i9383", prestamoGuardado.getIsbn());
        assertEquals("nicjef3", prestamoGuardado.getIdentificacionUsuario());
        assertNotEquals(2, prestamoGuardado.getTipoUsuario());
        assertNotNull(prestamoGuardado.getId());
        assertNotNull(prestamoGuardado.getFechaMaximaDevolucion());
    }

}
