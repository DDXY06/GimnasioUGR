package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Clase;
import com.example.gimnasiougr.Models.TipoClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {
    List<Clase> findByTipo(TipoClase tipoClase);
}
