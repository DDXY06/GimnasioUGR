package com.example.gimnasiougr.Services;

import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> buscarPorFiltro(String tipoFiltro, String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return listarTodos(); // Si no hay texto, devuelve todos
        }

        if ("dni".equalsIgnoreCase(tipoFiltro)) {
            return usuarioRepository.findByDniContainingIgnoreCase(textoBusqueda);
        } else if ("nombre".equalsIgnoreCase(tipoFiltro)) {
            return usuarioRepository.findByNombreContainingIgnoreCase(textoBusqueda);
        }

        return listarTodos(); // Por defecto
    }

}
