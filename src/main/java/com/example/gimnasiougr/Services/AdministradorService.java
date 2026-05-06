package com.example.gimnasiougr.Services;

import com.example.gimnasiougr.Models.Administrador;
import com.example.gimnasiougr.Repositories.AdministradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministradorService {
    private final AdministradorRepository administradorRepository;

    public List<Administrador> findAll() {
        return administradorRepository.findAll();
    }

    public Administrador findById(Long id) {
        if (id >= 1) return administradorRepository.findById(id).orElse(null);
        else return null;
    }

    public Administrador create(Administrador admin) {
        if (admin != null) return administradorRepository.save(admin);
        return null;
    }

    public Administrador update(Administrador admin) {
        if(admin != null) return administradorRepository.save(admin);
        return null;
    }

    public boolean deleteById(Long id) {
        if (administradorRepository.existsById(id)) {
            administradorRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
