package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Deporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeporteRepository extends JpaRepository<Deporte, Long> {
}
