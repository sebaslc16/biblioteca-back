package com.ceiba.biblioteca.serviceImpl;

import com.ceiba.biblioteca.model.Prestamo;
import com.ceiba.biblioteca.repository.PrestamoRepository;
import com.ceiba.biblioteca.service.PrestamoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public Prestamo save(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    @Override
    public Optional<Prestamo> findById(Long id) {
        return prestamoRepository.findById(id);
    }

    @Override
    public Optional<Prestamo> findByIdentificacionUsuario(String identificacionUsuario) {
        return prestamoRepository.findByIdentificacionUsuario(identificacionUsuario);
    }

    @Override
    public void delete(Long id) {
        prestamoRepository.deleteById(id);
    }
}
