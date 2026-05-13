package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Cliente;
import com.example.gimnasiougr.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByDniContainingIgnoreCase(String dni);
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    Optional<Cliente> findByUsuario(Usuario usuario);
}
