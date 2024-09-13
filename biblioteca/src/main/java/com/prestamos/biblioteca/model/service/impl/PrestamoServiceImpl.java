package com.prestamos.biblioteca.model.service.impl;

import com.prestamos.biblioteca.common.exception.ListaPrestamoException;
import com.prestamos.biblioteca.common.exception.PrestamoException;
import com.prestamos.biblioteca.common.util.constant.PrestamoConstant;
import com.prestamos.biblioteca.model.entity.Prestamo;
import com.prestamos.biblioteca.model.repository.PrestamoRepository;
import com.prestamos.biblioteca.model.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public Prestamo save(Prestamo prestamo) {
        if (prestamo.getTipoUsuario().equals(PrestamoConstant.USUARIO_INVITADO)) {
            Optional<Prestamo> prestamoInvitado = prestamoRepository.findByIdentificacionUsuario(prestamo.getIdentificacionUsuario());

            if (prestamoInvitado.isPresent()) {
                throw new PrestamoException(HttpStatus.BAD_REQUEST,"El usuario con identificación " + prestamo.getIdentificacionUsuario() +
                        " ya tiene un libro prestado por lo cual no se le puede realizar otro préstamo");
            }
        }
        prestamo.setFechaMaximaDevolucion(Prestamo.calcularFechaMaximaDevolucion(prestamo.getTipoUsuario()));
        return prestamoRepository.save(prestamo);
    }

    @Override
    public Prestamo findById(Long id) {
        Optional<Prestamo> prestamoConsultado = prestamoRepository.findById(id);

        if (!prestamoConsultado.isPresent()) {
            throw new PrestamoException(HttpStatus.BAD_REQUEST, "Prestamo con el id " + id + " no existe!");
        }
        return prestamoConsultado.get();
    }

    @Override
    public Prestamo findByIdentificacionUsuario(String identificacionUsuario) {
        Optional<Prestamo> prestamoConsultadoPorIdentificacion = prestamoRepository.findByIdentificacionUsuario(identificacionUsuario);
        if (!prestamoConsultadoPorIdentificacion.isPresent()) {
            throw new PrestamoException(HttpStatus.BAD_REQUEST, "Prestamo con la identificación de usuario " + identificacionUsuario + " no existe!");
        }
        return prestamoConsultadoPorIdentificacion.get();
    }

    @Override
    public void deleteById(Long id) {
        Optional<Prestamo> prestamoParaEliminar = prestamoRepository.findById(id);

        if(!prestamoParaEliminar.isPresent()) {
            throw new PrestamoException(HttpStatus.BAD_REQUEST, "No se pudo eliminar el prestamo por que no existe!");
        }
        prestamoRepository.deleteById(id);
    }

    @Override
    public List<Prestamo> findAll() {
        List<Prestamo> listaPrestamos = prestamoRepository.findAll();

        if(listaPrestamos.isEmpty()) {
            throw new ListaPrestamoException(HttpStatus.OK, "No se encontraron prestamos.", listaPrestamos);
        }

        return listaPrestamos;
    }
}
