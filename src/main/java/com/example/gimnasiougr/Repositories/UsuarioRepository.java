package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByDniContainingIgnoreCase(String dni);

    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
}
