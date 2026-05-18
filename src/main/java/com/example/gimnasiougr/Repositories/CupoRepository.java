package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CupoRepository extends JpaRepository<Cupo, Long> {

    long countByBonoId(Long bonoId);

    long countByClaseId(Long claseId);

    List<Cupo> findByBonoId(Long bonoId);

    List<Cupo> findByClaseId(Long claseId);

    List<Cupo> findByClienteIdAndClaseTipo(Long id, TipoClase tipoClase);

    Optional<Cupo> findByClienteIdAndClaseId(Long idCliente, Long idClase);

    boolean existsByClienteIdAndClaseId(Long clienteId, Long claseId);
}