package com.example.gimnasiougr.Repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Consulta para saber si tiene un bono válido
    @Query("SELECT b FROM Bono b WHERE b.cliente.id = :clienteId AND b.tipo = 'UNO' AND b.maxCupos > (SELECT COUNT(c) FROM Cupo c WHERE c.bono.id = b.id)")
    List<Bono> findBonosTipoUnoValidos(@Param("clienteId") Long clienteId);

    @Query("SELECT b FROM Bono b WHERE b.cliente.id = :clienteId AND b.tipo = 'DOS' AND b.maxCupos > (SELECT COUNT(c) FROM Cupo c WHERE c.bono.id = b.id)")
    List<Bono> findBonosTipoDosValidos(@Param("clienteId") Long clienteId);

}