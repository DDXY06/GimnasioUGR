package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Bono;
import com.example.gimnasiougr.Models.TipoBono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BonoRepository extends JpaRepository<Bono, Long> {
    List<Bono> findByFechaCompra(LocalDate fecha);

    List<Bono> findByClienteNombreContainingIgnoreCase(String nombreCliente);
    List<Bono> findByTipoIn(List<com.example.gimnasiougr.Models.TipoBono> tipos);
    List<Bono> findByClienteId(Long clienteId);

    boolean existsByClienteIdAndTipo(Long id, TipoBono tipoBono);

    List<Bono> findByTipo(TipoBono tipoCoincidente);

    Optional<Bono> findFirstByClienteIdAndMaxCuposGreaterThan(Long clienteId, int maxCupos);
}