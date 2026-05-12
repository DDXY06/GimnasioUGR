package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Cupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CupoRepository extends JpaRepository<Cupo, Long> {
    long countByBonoId(Long bonoId);
    List<Cupo> findByBonoId(Long bonoId);
}