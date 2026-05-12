package com.example.gimnasiougr.Services;

import com.example.gimnasiougr.Models.*;
import com.example.gimnasiougr.Repositories.ClaseRepository;
import com.example.gimnasiougr.Repositories.DeporteRepository;
import com.example.gimnasiougr.Repositories.EntrenadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final DeporteRepository deporteRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final CupoService cupoService;

    public ClaseService(ClaseRepository claseRepository,
                        DeporteRepository deporteRepository,
                        EntrenadorRepository entrenadorRepository,
                        CupoService cupoService) {
        this.claseRepository = claseRepository;
        this.deporteRepository = deporteRepository;
        this.entrenadorRepository = entrenadorRepository;
        this.cupoService = cupoService;
    }

    public List<ClaseDTO> listarTodos() {
        List<ClaseDTO> claseDTOs = new ArrayList<>();
        for (Clase clase : claseRepository.findAll()) {
            claseDTOs.add(mapToDTO(clase));
        }
        return claseDTOs;
    }

    @Transactional
    public ClaseDTO guardar(ClaseDTO claseDTO) {
        Clase clase = mapToEntity(claseDTO);
        Clase guardada = claseRepository.save(clase);
        return mapToDTO(guardada);
    }

    public ClaseDTO buscarPorId(Long id) {
        return mapToDTO(claseRepository.findById(id).orElse(null));
    }

    @Transactional
    public boolean eliminar(Long id) {
        if (claseRepository.existsById(id)) {
            claseRepository.findById(id).ifPresent(clase -> {
                claseRepository.delete(clase);
            });
            return true;
        }
        return false;
    }

    public List<ClaseDTO> buscarPorFiltro(String tipoFiltro, String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return listarTodos();
        }

        List<Clase> clases;
        if ("tipo".equalsIgnoreCase(tipoFiltro)) {
            List<Clase> matchingClases = new ArrayList<>();
            for (TipoClase t : TipoClase.values()) {
                if (t.name().equalsIgnoreCase(textoBusqueda) ||
                        (t.getNombre() != null && t.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase()))) {
                    matchingClases.addAll(claseRepository.findByTipoOrderByFechaAscHoraAsc(t));
                }
            }
            clases = matchingClases;
        } else if ("deporte".equalsIgnoreCase(tipoFiltro)) {
            clases = claseRepository.findByDeporteNombreContainingIgnoreCase(textoBusqueda);
        } else if ("entrenador".equalsIgnoreCase(tipoFiltro)) {
            clases = claseRepository.findByEntrenadorNombreContainingIgnoreCase(textoBusqueda);
        } else {
            clases = claseRepository.findAll();
        }

        return clases.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ClaseDTO mapToDTO(Clase clase) {
        if (clase == null) return null;

        ClaseDTO dto = new ClaseDTO();
        dto.setId(clase.getId());
        dto.setTipo(clase.getTipo());
        dto.setEstado(clase.getEstado());
        dto.setFecha(clase.getFecha());
        dto.setHora(clase.getHora());
        dto.setMaxCupos(clase.getMaxCupos());

        if (clase.getDeporte() != null) {
            dto.setDeporteId(clase.getDeporte().getId());
            dto.setNombreDeporte(clase.getDeporte().getNombre());
        }

        if (clase.getEntrenador() != null) {
            dto.setEntrenadorId(clase.getEntrenador().getId());
            dto.setNombreEntrenador(clase.getEntrenador().getNombre());
        }

        if (clase.getCupos() != null) {
            List<CupoDTO> cuposDTO = new ArrayList<>();
            for (Cupo cupo : clase.getCupos()) {
                cuposDTO.add(cupoService.mapToDTO(cupo));
            }
            dto.setCuposDTO(cuposDTO);
        }

        return dto;
    }

    private Clase mapToEntity(ClaseDTO dto) {
        Clase clase = new Clase();

        if (dto.getId() != null) {
            clase = claseRepository.findById(dto.getId()).orElse(new Clase());
        }

        clase.setTipo(dto.getTipo());
        clase.setEstado(dto.getEstado());
        clase.setFecha(dto.getFecha());
        clase.setHora(dto.getHora());
        clase.setMaxCupos(dto.getMaxCupos());

        if (dto.getDeporteId() != null) {
            clase.setDeporte(deporteRepository.findById(dto.getDeporteId()).orElse(null));
        }

        if (dto.getEntrenadorId() != null) {
            clase.setEntrenador(entrenadorRepository.findById(dto.getEntrenadorId()).orElse(null));
        }

        return clase;
    }
}
