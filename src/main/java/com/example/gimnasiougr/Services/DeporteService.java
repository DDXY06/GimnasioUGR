package com.example.gimnasiougr.Services;

import com.example.gimnasiougr.Models.Deporte;
import com.example.gimnasiougr.Repositories.DeporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeporteService {

    private final DeporteRepository deporteRepository;

    public List<Deporte> listarTodos() {
        return deporteRepository.findAll();
    }

    @Transactional
    public Deporte guardar(Deporte deporte) {
        if (deporte != null) {
            return deporteRepository.save(deporte);
        }
        return null;
    }

    @Transactional
    public boolean eliminar(Long id) {
        if (deporteRepository.existsById(id)) {
            deporteRepository.findById(id).ifPresent(deporte -> {
                deporteRepository.delete(deporte);
            });
            return true;
        }
        return false;
    }
}
