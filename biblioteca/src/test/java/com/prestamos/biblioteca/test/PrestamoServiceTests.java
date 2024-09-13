package com.prestamos.biblioteca.test;

import com.prestamos.biblioteca.common.exception.ListaPrestamoException;
import com.prestamos.biblioteca.common.exception.PrestamoException;
import com.prestamos.biblioteca.model.entity.Prestamo;
import com.prestamos.biblioteca.model.repository.PrestamoRepository;
import com.prestamos.biblioteca.model.service.PrestamoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
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
        when(prestamoRepository.findById(5L)).thenThrow(PrestamoException.class);

        Prestamo prestamoEncontrado = prestamoService.findById(2L);
        Prestamo prestamoIgual = prestamoService.findById(2L);

        assertThrows(PrestamoException.class, () -> {
            prestamoService.findById(5L);
        });
        assertSame(prestamoEncontrado, prestamoIgual);
        assertEquals(prestamoEncontrado.getId(),prestamoIgual.getId());
        assertEquals("383KS", prestamoEncontrado.getIsbn());
        assertEquals("usuario2", prestamoIgual.getIdentificacionUsuario());
        assertFalse(prestamoEncontrado.getTipoUsuario() != 2);

        verify(prestamoRepository, times(2)).findById(2L);
        verify(prestamoRepository, never()).findById(1L);
    }

    @Test
    @Tag("service")
    @DisplayName("Test de metodo findByIdentificacionUsuario desde el service")
    void testfindByIdentificacionUsuarioService() {

        when(prestamoRepository.findByIdentificacionUsuario("usuario3")).thenReturn(Optional.ofNullable(prestamo3));
        when(prestamoRepository.findByIdentificacionUsuario("noexiste")).thenReturn(Optional.empty());

        Optional<Prestamo> prestamoUsuario3 = Optional.ofNullable(prestamoService.findByIdentificacionUsuario("usuario3"));

        assertThrows(PrestamoException.class, () -> {
            prestamoService.findByIdentificacionUsuario("noexiste");
        });
        assertTrue(prestamoUsuario3.isPresent());
        assertEquals("usuario3", prestamoUsuario3.get().getIdentificacionUsuario());

        verify(prestamoRepository, times(1)).findByIdentificacionUsuario("usuario3");
        verify(prestamoRepository, times(1)).findByIdentificacionUsuario("noexiste");
        verify(prestamoRepository, times(2)).findByIdentificacionUsuario(anyString());
        verify(prestamoRepository, never()).findByIdentificacionUsuario("usuario2");
    }

    @Test
    @Tag("service")
    @DisplayName("Test de metodo findAll desde el service")
    void testFindAllService() {
        List<Prestamo> listadoPrestamos = new ArrayList<>();
        listadoPrestamos.add(prestamo1);
        listadoPrestamos.add(prestamo2);

        when(prestamoRepository.findAll()).thenReturn(listadoPrestamos);

        List<Prestamo> listadoPrestamosConsultados = prestamoService.findAll();

        assertTrue(listadoPrestamosConsultados.size() > 1);
        assertEquals("98493PS", listadoPrestamosConsultados.get(0).getIsbn());

        verify(prestamoRepository, times(1)).findAll();
    }

    @Test
    @Tag("service")
    @DisplayName("Test de metodo findAll desde el service retornado exception")
    void testFindAllServiceException() {
        List<Prestamo> listadoPrestamos = new ArrayList<>();

        when(prestamoRepository.findAll()).thenReturn(listadoPrestamos);

        assertThrows(ListaPrestamoException.class, () -> {
            prestamoService.findAll();
        });
        assertTrue(listadoPrestamos.isEmpty());
        verify(prestamoRepository, times(1)).findAll();
    }
}
