package com.ceiba.biblioteca.test;

import com.ceiba.biblioteca.model.Prestamo;
import com.ceiba.biblioteca.repository.PrestamoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("test_integracion_jpa")
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        prestamoNuevo.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamoNuevo.getTipoUsuario()));

        prestamoRepository.save(prestamoNuevo);

        Optional<Prestamo> prestamo1 = prestamoRepository.findById(1L);
        assertTrue(prestamo1.isPresent());
    }

    @Test
    @Tag("repository")
    @DisplayName("Test del metodo findByIsnb")
    @Order(2)
    void testFindByIsbnPrestamo() {
        Optional<Prestamo> prestamoNoExistente = prestamoRepository.findByIsbn("noexiste");

        assertFalse(prestamoNoExistente.isPresent());

        Prestamo prestamoNuevo = new Prestamo("existe", "4849fj", 3);
        prestamoNuevo.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamoNuevo.getTipoUsuario()));

        prestamoRepository.save(prestamoNuevo);

        Optional<Prestamo> prestamo1 = prestamoRepository.findByIsbn("existe");
        assertTrue(prestamo1.isPresent());
        assertEquals("existe", prestamo1.get().getIsbn());
    }

    @Test
    @Tag("repository")
    @DisplayName("Test del metodo save")
    @Order(3)
    void testSavePrestamo() {
        Prestamo prestamoNuevo = new Prestamo("i9383", "nicjef3", 1);
        prestamoNuevo.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamoNuevo.getTipoUsuario()));

        Prestamo prestamoGuardado = prestamoRepository.save(prestamoNuevo);

        assertNotNull(prestamoGuardado);
        assertEquals("i9383", prestamoGuardado.getIsbn());
        assertEquals("nicjef3", prestamoGuardado.getIdentificacionUsuario());
        assertNotEquals(2, prestamoGuardado.getTipoUsuario());
        assertNotNull(prestamoGuardado.getId());
        assertNotNull(prestamoGuardado.getFechaMaximaDevolucion());
    }

}
