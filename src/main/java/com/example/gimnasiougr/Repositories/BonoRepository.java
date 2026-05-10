package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Bono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BonoRepository extends JpaRepository<Bono, Long> {
    List<Bono> findByFechaCompra(LocalDate fecha);
}
