package com.example.gimnasiougr.Repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gimnasiougr.Models.Bono;
import com.example.gimnasiougr.Models.TipoBono;

@Repository
public interface BonoRepository extends JpaRepository<Bono, Long> {
    List<Bono> findByFechaCompra(LocalDate fecha);

    List<Bono> findByClienteNombreContainingIgnoreCase(String nombreCliente);
    List<Bono> findByClienteId(Long clienteId);

    boolean existsByClienteIdAndTipo(Long id, TipoBono tipoBono);

    List<Bono> findByTipo(TipoBono tipo);

    Optional<Bono> findFirstByClienteIdAndMaxCuposGreaterThan(Long clienteId, int maxCupos);

    Optional<Bono> findFirstByTipoAndMaxCuposGreaterThanOrderByFechaCompraAsc(TipoBono tipo, int maxCupos);

    List<Bono> findByTipoOrderByFechaCompraAsc(TipoBono tipo);
}