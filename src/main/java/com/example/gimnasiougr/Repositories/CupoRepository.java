package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CupoRepository extends JpaRepository<Cupo, Long> {
    long countByBonoId(Long bonoId);
    List<Cupo> findByBonoId(Long bonoId);

    List<Cupo> findByUsuarioIdAndClaseTipo(Long id, TipoClase tipoClase);

    Optional<Cupo> findByUsuarioIdAndClaseId(Long id, Long id1);

    boolean existsByUsuarioAndClase(Cliente cliente, Clase clase);

    long countByClase(Clase clase);

    long countByBono(Bono b);

    List<Cupo> findByClaseId(Long claseId);

    Cupo findByClase_IdAndUsuario_Id(Long claseId, Long clienteId);

    long countByClaseId(Long claseId);
}