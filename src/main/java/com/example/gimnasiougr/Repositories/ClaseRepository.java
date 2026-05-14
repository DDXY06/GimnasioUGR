package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Clase;
import com.example.gimnasiougr.Models.TipoClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {
    List<Clase> findByTipoOrderByFechaAscHoraAsc(TipoClase tipo);
    List<Clase> findByDeporteNombreContainingIgnoreCase(String nombre);
    List<Clase> findByEntrenadorNombreContainingIgnoreCase(String nombre);
    List<Clase> findByFecha(LocalDate fecha);
    List<Clase> findByHora(LocalTime hora);
    List<Clase> findByFechaAndHora(LocalDate fecha, LocalTime hora);
}
