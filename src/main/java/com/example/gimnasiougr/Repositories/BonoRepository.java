package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Bono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BonoRepository extends JpaRepository<Bono, Long> {
    List<Bono> findByFechaCompra(LocalDate fecha);

    List<Bono> findByClienteNombreContainingIgnoreCase(String nombreCliente);
    List<Bono> findByTipoIn(List<com.example.gimnasiougr.Models.TipoBono> tipos);
    List<Bono> findByClienteId(Long clienteId);
}