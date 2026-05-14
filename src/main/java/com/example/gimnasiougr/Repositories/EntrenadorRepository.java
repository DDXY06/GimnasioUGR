package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
    List<Entrenador> findByDniContainingIgnoreCase(String dni);
    List<Entrenador> findByNombreContainingIgnoreCase(String nombre);
}
