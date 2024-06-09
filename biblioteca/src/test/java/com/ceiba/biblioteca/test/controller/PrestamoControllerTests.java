package com.ceiba.biblioteca.test.controller;

import com.ceiba.biblioteca.calificador.SolicitudPrestarLibroTest;
import com.ceiba.biblioteca.controller.PrestamoController;
import com.ceiba.biblioteca.model.Prestamo;
import com.ceiba.biblioteca.service.PrestamoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(PrestamoController.class)
@Tag("controller_mvc_test")
public class PrestamoControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PrestamoService prestamoService;

    @Autowired
    ObjectMapper objectMapper;

    Prestamo prestamo1 = new Prestamo("98493PS", "usuario1", 1);
    Prestamo prestamo2 = new Prestamo("383KS", "usuario2", 2);
    Prestamo prestamo3 = new Prestamo("2193JS", "usuario3", 3);

    Prestamo prestamo4 = new Prestamo("7833DS", "usuario4", 4);

    @Test
    @Tag("controller")
    void testConsultarPrestamoExitoso() throws Exception {

        prestamo1.setId(1L);
        prestamo1.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamo1.getTipoUsuario()));
        when(prestamoService.findById(1L)).thenReturn(Optional.ofNullable(prestamo1));

        mvc.perform(get("/prestamo/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.isbn").value("98493PS"))
                .andExpect(jsonPath("$.identificacionUsuario").value("usuario1"))
                .andExpect(jsonPath("$.tipoUsuario").value(1))
                .andExpect(jsonPath("$.fechaMaximaDevolucion").exists())
                .andExpect(jsonPath("$.fechaMaximaDevolucion").isString())
                .andExpect(jsonPath("$.fechaMaximaDevolucion").value(Prestamo.calcularFechaMaximaDevolucion(1)));
        verify(prestamoService).findById(1L);
        verify(prestamoService,times(1)).findById(anyLong());
    }

    @Test
    @Tag("controller")
    public void testGuardarPrestamoExitoso() throws Exception {

        Prestamo prestamoNuevo = new Prestamo("9303JF", "usuario2", 2);
        when(prestamoService.save(any())).then(invocation -> {
            Prestamo p = invocation.getArgument(0);
            p.setId(2L);
            return p;
        });

        mvc.perform(post("/prestamo").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prestamoNuevo)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fechaMaximaDevolucion").exists());
    }

    @Test
    @Tag("controller")
    public void testGuardarPrestamoErrorTipoUsuarioNoPermitido() throws Exception {

        Prestamo prestamoTipoUsuarioInvalido = new Prestamo("62994DV", "usuario4", 4);
        when(prestamoService.save(any())).then(invocation -> {
            Prestamo p = invocation.getArgument(0);
            p.setId(4L);
            return p;
        });

        mvc.perform(post("/prestamo").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamoTipoUsuarioInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.mensaje").value("Tipo de usuario no permitido en la biblioteca"));
    }

    @Test
    @Tag("controller")
    public void testGuardarPrestamoErrorIsbnConMas10Digitos() throws Exception {

        Prestamo prestamoIsbnInvalido = new Prestamo("62994482942DV", "usuario3", 3);
        when(prestamoService.save(any())).then(invocation -> {
            Prestamo p = invocation.getArgument(0);
            p.setId(3L);
            return p;
        });

        mvc.perform(post("/prestamo").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamoIsbnInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.mensaje").value("Son permitidos mínimo 1 y máximo 10 dígitos para el isbn."));
    }

    @Test
    @Tag("controller")
    public void testGuardarPrestamoErrorIdentificacionUsuarioConMas10Digitos() throws Exception {

        Prestamo prestamoIsbnInvalido = new Prestamo("89942LV", "usuarioInvalido", 2);
        when(prestamoService.save(any())).then(invocation -> {
            Prestamo p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        mvc.perform(post("/prestamo").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamoIsbnInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.mensaje").value("Son permitidos mínimo 1 y máximo 10 dígitos para la identificación del usuario."));
    }
}
