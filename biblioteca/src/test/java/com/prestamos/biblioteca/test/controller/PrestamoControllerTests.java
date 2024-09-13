package com.prestamos.biblioteca.test.controller;

import com.prestamos.biblioteca.common.exception.ListaPrestamoException;
import com.prestamos.biblioteca.common.exception.PrestamoException;
import com.prestamos.biblioteca.controller.rest.PrestamoController;
import com.prestamos.biblioteca.model.entity.Prestamo;
import com.prestamos.biblioteca.model.service.PrestamoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    Prestamo prestamo3 = new Prestamo("2193JS", "usuario3", 3);

    @Test
    @Tag("controller")
    void testConsultarPrestamoNoExistente() throws Exception {

        when(prestamoService.findById(7L)).thenThrow(new PrestamoException(HttpStatus.BAD_REQUEST, "Prestamo con el id 7 no existe!"));

        mvc.perform(get("/prestamo/7").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.mensaje").value("Prestamo con el id 7 no existe!"));

        verify(prestamoService).findById(7L);
        verify(prestamoService, times(1)).findById(anyLong());
    }

    @Test
    @Tag("controller")
    void testConsultarPrestamoExitoso() throws Exception {

        prestamo1.setId(1L);
        prestamo1.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamo1.getTipoUsuario()));
        when(prestamoService.findById(1L)).thenReturn(prestamo1);

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
        verify(prestamoService, times(1)).findById(anyLong());
    }

    @Test
    @Tag("controller")
    public void testGuardarPrestamoExitoso() throws Exception {

        Prestamo prestamoNuevo = new Prestamo("9303JF", "usuario2", 2);
        prestamoNuevo.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamoNuevo.getTipoUsuario()));
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

        verify(prestamoService).save(any());
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

        verify(prestamoService,never()).save(any());
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

    @Test
    @Tag("controller")
    public void testGuardarPrestamoErrorIsbnVacio() throws Exception {

        Prestamo prestamoIsbnVacio = new Prestamo("", "sinIsbn", 1);
        when(prestamoService.save(any())).then(invocation -> {
            Prestamo p = invocation.getArgument(0);
            p.setId(2L);
            return p;
        });

        mvc.perform(post("/prestamo").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamoIsbnVacio)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.mensaje").value("Son permitidos mínimo 1 y máximo 10 dígitos para el isbn."));
    }

    @Test
    @Tag("controller")
    public void testGuardarPrestamoErrorIdentificacionUsuarioNull() throws Exception {

        Prestamo prestamoIdentificacionNull = new Prestamo("33400TY", null, 1);
        when(prestamoService.save(any())).then(invocation -> {
            Prestamo p = invocation.getArgument(0);
            p.setId(5L);
            return p;
        });

        mvc.perform(post("/prestamo").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamoIdentificacionNull)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.mensaje").value("Ingrese una identificación de usuario valida."));
    }

    @Test
    @Tag("controller")
    void testConsultarPorIdentificacionUsuarioExitoso() throws Exception {

        prestamo3.setId(3L);
        prestamo3.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamo3.getTipoUsuario()));
        when(prestamoService.findByIdentificacionUsuario(prestamo3.getIdentificacionUsuario())).thenReturn(prestamo3);

        mvc.perform(get("/prestamo-identificacion/"+prestamo3.getIdentificacionUsuario()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.isbn").value(prestamo3.getIsbn()))
                .andExpect(jsonPath("$.fechaMaximaDevolucion").value(Prestamo.calcularFechaMaximaDevolucion(prestamo3.getTipoUsuario())));

        verify(prestamoService,times(1)).findByIdentificacionUsuario(prestamo3.getIdentificacionUsuario());
    }

    @Test
    void testConsultarPorIdentificacionUsuarioErrorNoExiste() throws Exception {

        when(prestamoService.findByIdentificacionUsuario("usuario")).thenThrow(new PrestamoException(HttpStatus.BAD_REQUEST, "Prestamo con la identificación de usuario usuario no existe!"));

        mvc.perform(get("/prestamo-identificacion/usuario").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.mensaje").value("Prestamo con la identificación de usuario usuario no existe!"));

        verify(prestamoService,times(1)).findByIdentificacionUsuario(anyString());
    }

    @Test
    void testConsultarListadoPrestamos() throws Exception {

        List<Prestamo> listadoPrestamos = new ArrayList<>();
        listadoPrestamos.add(prestamo1);
        listadoPrestamos.add(prestamo3);

        when(prestamoService.findAll()).thenReturn(listadoPrestamos);

        mvc.perform(get("/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(prestamoService, times(1)).findAll();
    }

    @Test
    void testConsultarListadoPrestamosException() throws Exception {

        when(prestamoService.findAll()).thenThrow(new ListaPrestamoException(HttpStatus.BAD_REQUEST,"", new ArrayList<>()));

        mvc.perform(get("/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(prestamoService, times(1)).findAll();
    }
}
