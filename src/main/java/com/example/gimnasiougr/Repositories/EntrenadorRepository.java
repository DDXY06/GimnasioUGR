package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Entrenador;
import com.example.gimnasiougr.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
    Optional<Entrenador> findFirstByUsuario(Usuario usuario);
    List<Entrenador> findByDniContainingIgnoreCase(String dni);
    List<Entrenador> findByNombreContainingIgnoreCase(String nombre);
}
