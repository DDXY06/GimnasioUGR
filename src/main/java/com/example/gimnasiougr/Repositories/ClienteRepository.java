package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByDniContainingIgnoreCase(String dni);
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
}
