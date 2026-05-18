package com.example.gimnasiougr.Repositories;

import com.example.gimnasiougr.Models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByDniContainingIgnoreCase(String dni);
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    Optional<Cliente> findByUsuarioId(Long Idusuario);

    // Consulta para traer los clientes que NO están en la tabla Cupo para esa clase
    @Query("SELECT c FROM Cliente c WHERE c.id NOT IN (SELECT cu.cliente.id FROM Cupo cu WHERE cu.clase.id = :claseId)")
    List<Cliente> findClientesNoInscritosEnClase(@Param("claseId") Long claseId);
}
