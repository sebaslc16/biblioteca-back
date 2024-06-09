package com.ceiba.biblioteca.test.controller;

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

import java.util.Optional;

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

        when(prestamoService.findById(7L)).thenReturn(Optional.empty());

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
        verify(prestamoService, times(1)).findById(anyLong());
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
    public void testGuardarOtroPrestamoErrorUsuarioInvitado() throws Exception {

        prestamo1.setId(3L);
        prestamo3.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamo3.getTipoUsuario()));
        when(prestamoService.findByIdentificacionUsuario(prestamo3.getIdentificacionUsuario())).thenReturn(Optional.ofNullable(prestamo3));

        Prestamo prestamoUsuarioInvitado = new Prestamo("049492NC", "usuario3", 3);
        when(prestamoService.save(any())).then(invocation -> {
            Prestamo p = invocation.getArgument(0);
            p.setId(5L);
            return p;
        });

        mvc.perform(post("/prestamo").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamoUsuarioInvitado)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.mensaje").value("El usuario con identificación " + prestamoUsuarioInvitado.getIdentificacionUsuario() +
                                                        " ya tiene un libro prestado por lo cual no se le puede realizar otro préstamo"));

        verify(prestamoService, never()).save(any());
        verify(prestamoService, times(1)).findByIdentificacionUsuario(prestamoUsuarioInvitado.getIdentificacionUsuario());
    }

    @Test
    @Tag("controller")
    void testConsultarPorIdentificacionUsuarioExitoso() throws Exception {

        prestamo3.setId(3L);
        prestamo3.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamo3.getTipoUsuario()));
        when(prestamoService.findByIdentificacionUsuario(prestamo3.getIdentificacionUsuario())).thenReturn(Optional.ofNullable(prestamo3));

        mvc.perform(get("/prestamo-identificacion/"+prestamo3.getIdentificacionUsuario()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.isbn").value(prestamo3.getIsbn()))
                .andExpect(jsonPath("$.fechaMaximaDevolucion").value(Prestamo.calcularFechaMaximaDevolucion(prestamo3.getTipoUsuario())));

        verify(prestamoService,times(1)).findByIdentificacionUsuario(prestamo3.getIdentificacionUsuario());
    }

    @Test
    void testConsultarPorIdentificacionUsuarioErrorNoExiste() throws Exception {

        when(prestamoService.findByIdentificacionUsuario("usuario")).thenReturn(Optional.empty());

        mvc.perform(get("/prestamo-identificacion/usuario").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.mensaje").value("Prestamo con la identificación de usuario usuario no existe!"));

        verify(prestamoService,times(1)).findByIdentificacionUsuario(anyString());
    }
}
