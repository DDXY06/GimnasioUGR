package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
}
