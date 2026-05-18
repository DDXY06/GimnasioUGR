package com.example.gimnasiougr.Services;

import com.example.gimnasiougr.Models.*;
import com.example.gimnasiougr.Repositories.EntrenadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntrenadorService {

    private final EntrenadorRepository entrenadorRepository;

    public List<EntrenadorDTO> listarTodos() {
        List<EntrenadorDTO> entrenadorDTOS = new ArrayList<>();
        for (Entrenador entrenador : entrenadorRepository.findAll()) {
            entrenadorDTOS.add(mapToDTO(entrenador));
        }
        return entrenadorDTOS;
    }

    @Transactional
    public EntrenadorDTO guardar(EntrenadorDTO entrenadorDTO) {
        Entrenador entrenador = mapToEntity(entrenadorDTO);
        Entrenador guardado = entrenadorRepository.save(entrenador);
        return mapToDTO(guardado);
    }

    public EntrenadorDTO buscarPorId(Long id) {
        return mapToDTO(entrenadorRepository.findById(id).orElse(null));
    }

    @Transactional
    public boolean eliminar(Long id) {
        //Comprobamos que el entrenador exista
        if (entrenadorRepository.existsById(id)) {
            entrenadorRepository.findById(id).ifPresent(entrenador -> {
                entrenadorRepository.delete(entrenador);
            });
            return true;
        }
        return false;
    }

    public List<EntrenadorDTO> buscarPorFiltro(String tipoFiltro, String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return listarTodos();
        }

        List<Entrenador> entrenadores;
        String filtroSeguro = (tipoFiltro != null) ? tipoFiltro.toLowerCase() : "";

        switch (filtroSeguro) {
            case "dni":
                entrenadores = entrenadorRepository.findByDniContainingIgnoreCase(textoBusqueda);
                break;

            case "nombre":
                entrenadores = entrenadorRepository.findByNombreContainingIgnoreCase(textoBusqueda);
                break;

            default:
                // Si no tiene filtro mostramos todo
                entrenadores = entrenadorRepository.findAll();
                break;
        }

        // Convertimos la lista de Entrenadores a DTOs
        List<EntrenadorDTO> resultadoDTO = new ArrayList<>();

        for (Entrenador entrenador : entrenadores) {
            EntrenadorDTO dto = this.mapToDTO(entrenador);
            resultadoDTO.add(dto);
        }

        return resultadoDTO;
    }

    public EntrenadorDTO mapToDTO(Entrenador entrenador) {
        if (entrenador == null) return null;
        EntrenadorDTO edto = new EntrenadorDTO();
        edto.setId(entrenador.getId());
        if (entrenador.getUsuario() != null) {
            edto.setUsuarioId(entrenador.getUsuario().getId());
            edto.setCorreo(entrenador.getUsuario().getCorreo());
        }
        edto.setNombre(entrenador.getNombre());
        edto.setDni(entrenador.getDni());
        edto.setTelf(entrenador.getTelf());

        return edto;
    }

    private Entrenador mapToEntity(EntrenadorDTO edto) {
        Entrenador entrenador = new Entrenador();

        // Si el DTO trae un ID, significa que estamos editando un entrenador existente.
        // Lo buscamos en la base de datos para no perder sus datos.
        if (edto.getId() != null) {
            entrenador = entrenadorRepository.findById(edto.getId()).orElse(new Entrenador());
        }

        // Asignamos los campos que SÍ existen en el modelo
        entrenador.setDni(edto.getDni());
        entrenador.setNombre(edto.getNombre());
        entrenador.setTelf(edto.getTelf());

        // Si el entrenador no tiene un usuario asignado (es nuevo), lo creamos
        if (entrenador.getUsuario() == null) {
            Usuario usuario = new Usuario();
            usuario.setRol(TipoUsuario.ENTRENADOR);
            entrenador.setUsuario(usuario);
        }

        // Asignamos el correo y contraseña al Usuario
        if (edto.getCorreo() != null) {
            entrenador.getUsuario().setCorreo(edto.getCorreo());
        }

        // Solo actualizamos la contraseña si nos enviaron una nueva
        if (edto.getContrasenia() != null && !edto.getContrasenia().isEmpty()) {
            entrenador.getUsuario().setContrasenia(edto.getContrasenia());
        }

        return entrenador;
    }
}
