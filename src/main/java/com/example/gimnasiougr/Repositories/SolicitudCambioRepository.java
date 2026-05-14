package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Estado;
import com.example.gimnasiougr.Models.SolicitudCambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudCambioRepository extends JpaRepository<SolicitudCambio, Long> {
    boolean existsByCupoIdAndEstado(Long cupoId, Estado estado);
}
