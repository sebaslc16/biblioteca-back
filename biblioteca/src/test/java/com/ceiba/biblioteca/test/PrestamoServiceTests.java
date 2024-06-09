package com.ceiba.biblioteca.test;

import com.ceiba.biblioteca.model.Prestamo;
import com.ceiba.biblioteca.repository.PrestamoRepository;
import com.ceiba.biblioteca.service.PrestamoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class PrestamoServiceTests {

    @MockBean
    PrestamoRepository prestamoRepository;

    @Autowired
    PrestamoService prestamoService;

    Prestamo prestamo1 = new Prestamo("98493PS", "usuario1", 1);
    Prestamo prestamo2 = new Prestamo("383KS", "usuario2", 2);
    Prestamo prestamo3 = new Prestamo("2193JS", "usuario3", 3);

    @BeforeEach
    void setUp() {
        prestamo1.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamo1.getTipoUsuario()));
        prestamo2.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamo2.getTipoUsuario()));
        prestamo3.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamo3.getTipoUsuario()));
    }


    @Test
    @Tag("service")
    @DisplayName("Test de save desde service")
    void testSaveService() {
        //GIVEN
        when(prestamoRepository.save(any(Prestamo.class))).then(invocationOnMock -> {
            Prestamo prestamo = invocationOnMock.getArgument(0);
            prestamo.setId(1L);
            return prestamo;
        });

        //WHEN
        Prestamo prestamoNuevo = prestamoService.save(prestamo1);

        //THEN
        assertNotNull(prestamoNuevo.getId());
        assertEquals(1L, prestamoNuevo.getId());
        assertEquals("98493PS", prestamoNuevo.getIsbn());
        assertEquals("usuario1", prestamoNuevo.getIdentificacionUsuario());
        assertEquals(1, prestamoNuevo.getTipoUsuario());
        assertNotNull(prestamoNuevo.getFechaMaximaDevolucion());
        assertEquals(Prestamo.calcularFechaMaximaDevolucion(prestamoNuevo.getTipoUsuario()), prestamoNuevo.getFechaMaximaDevolucion());

        verify(prestamoRepository).save(any(Prestamo.class));
    }

    @Test
    @Tag("service")
    @DisplayName("Test de metodo findById desde el service")
    void testFindByIdService() {
        when(prestamoRepository.findById(2L)).thenReturn(Optional.ofNullable(prestamo2));

        Optional<Prestamo> prestamoEncontrado = prestamoService.findById(2L);
        Optional<Prestamo> prestamoIgual = prestamoService.findById(2L);

        assertTrue(prestamoEncontrado.isPresent());
        assertTrue(prestamoIgual.isPresent());
        assertSame(prestamoEncontrado, prestamoIgual);
        assertEquals(prestamoEncontrado.get().getId(),prestamoIgual.get().getId());
        assertEquals("383KS", prestamoEncontrado.get().getIsbn());
        assertEquals("usuario2", prestamoIgual.get().getIdentificacionUsuario());
        assertFalse(prestamoEncontrado.get().getTipoUsuario() != 2);

        verify(prestamoRepository, times(2)).findById(2L);
        verify(prestamoRepository, never()).findById(1L);
    }

    @Test
    @Tag("service")
    @DisplayName("Test de metodo findByIdentificacionUsuario desde el service")
    void testfindByIdentificacionUsuarioService() {

        when(prestamoRepository.findByIdentificacionUsuario("usuario3")).thenReturn(Optional.ofNullable(prestamo3));
        when(prestamoRepository.findByIdentificacionUsuario("noexiste")).thenReturn(Optional.empty());

        Optional<Prestamo> prestamoUsuario3 = prestamoService.findByIdentificacionUsuario("usuario3");
        Optional<Prestamo> prestamoNoExistente = prestamoService.findByIdentificacionUsuario("noexiste");

        assertTrue(prestamoUsuario3.isPresent());
        assertFalse(prestamoNoExistente.isPresent());
        assertEquals(Optional.empty(), prestamoNoExistente);
        assertEquals("usuario3", prestamoUsuario3.get().getIdentificacionUsuario());

        verify(prestamoRepository, times(1)).findByIdentificacionUsuario("usuario3");
        verify(prestamoRepository, times(1)).findByIdentificacionUsuario("noexiste");
        verify(prestamoRepository, times(2)).findByIdentificacionUsuario(anyString());
        verify(prestamoRepository, never()).findByIdentificacionUsuario("usuario2");
    }
}
