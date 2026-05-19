package com.example.gimnasiougr.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gimnasiougr.Models.Bono;
import com.example.gimnasiougr.Models.Clase;
import com.example.gimnasiougr.Models.ClaseDTO;
import com.example.gimnasiougr.Models.Cliente;
import com.example.gimnasiougr.Models.Cupo;
import com.example.gimnasiougr.Models.CupoDTO;
import com.example.gimnasiougr.Models.Estado;
import com.example.gimnasiougr.Models.TipoClase;
import com.example.gimnasiougr.Repositories.BonoRepository;
import com.example.gimnasiougr.Repositories.ClaseRepository;
import com.example.gimnasiougr.Repositories.ClienteRepository;
import com.example.gimnasiougr.Repositories.CupoRepository;
import com.example.gimnasiougr.Repositories.DeporteRepository;
import com.example.gimnasiougr.Repositories.EntrenadorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final DeporteRepository deporteRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final CupoService cupoService;
    private final ClienteRepository clienteRepository;
    private final CupoRepository cupoRepository;
    private final BonoRepository bonoRepository;

    public List<ClaseDTO> listarTodos() {
        List<ClaseDTO> claseDTOs = new ArrayList<>();
        for (Clase clase : claseRepository.findAll()) {
            claseDTOs.add(mapToDTO(clase));
        }
        return claseDTOs;
    }

    public List<ClaseDTO> buscarClasesTipo2Pendiente() {
        List<ClaseDTO> claseDTOs = new ArrayList<>();
        for (Clase clase : claseRepository.findClasesTipo2Pendiente()) {
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
        //Comprobamos si la clase existe
        if (claseRepository.existsById(id)) {
            claseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void aceptarClaseTipo2(ClaseDTO claseDTO) {
        if(claseDTO.getTipo() == TipoClase.DOS){
            List<Cupo> cupoClase = cupoRepository.findByClaseId(claseDTO.getId());
            if (!cupoClase.isEmpty()) {
                cupoService.aceptarCupoTipo2(cupoClase.getFirst());
                Clase clase = mapToEntity(claseDTO);
                clase.setEstado(Estado.CONFIRMADO);
                claseRepository.save(clase);
            }
        }
    }

    @Transactional
    public void rechazarClaseTipo2(ClaseDTO claseDTO){
        if(claseDTO.getTipo() == TipoClase.DOS){
            List<Cupo> cuposClase = cupoRepository.findByClaseId(claseDTO.getId());
            if (!cuposClase.isEmpty()) {
                cupoService.rechazarCupoTipo2(cuposClase.getFirst());
                Clase clase = mapToEntity(claseDTO);
                clase.setEstado(Estado.CANCELADO);
                claseRepository.save(clase);
            }
        }
    }

    public List<ClaseDTO> buscarPorFiltro(String tipoFiltro, String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return listarTodos();
        }

        List<Clase> clases = new ArrayList<>();

        String filtroSeguro = (tipoFiltro != null) ? tipoFiltro.toLowerCase() : "";

        // Búsqueda con switch
        switch (filtroSeguro) {
            case "tipo":
                String textoMinusculas = textoBusqueda.toLowerCase();
                for (TipoClase tipoEnum : TipoClase.values()) {

                    boolean coincideNombreEnum = tipoEnum.name().equalsIgnoreCase(textoBusqueda);
                    boolean tieneNombre = tipoEnum.getNombre() != null;
                    boolean coincideAtributo = tieneNombre && tipoEnum.getNombre().toLowerCase().contains(textoMinusculas);

                    if (coincideNombreEnum || coincideAtributo) {
                        clases.addAll(claseRepository.findByTipoOrderByFechaAscHoraAsc(tipoEnum));
                    }
                }
                break;

            case "deporte":
                clases = claseRepository.findByDeporteNombreContainingIgnoreCase(textoBusqueda);
                break;

            case "entrenador":
                clases = claseRepository.findByEntrenadorNombreContainingIgnoreCase(textoBusqueda);
                break;

            default:
                // Si no tiene filtro, devolvemos todas las clases
                clases = claseRepository.findAll();
                break;
        }

        // Convertimos a DTO
        List<ClaseDTO> listaResultadoDTO = new ArrayList<>();
        for (Clase clase : clases) {
            ClaseDTO dto = this.mapToDTO(clase);
            listaResultadoDTO.add(dto);
        }

        return listaResultadoDTO;
    }

    @Transactional
    public void addCliente(Long claseId, Long clienteId) {

        Clase clase = claseRepository.findById(claseId).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Llamamos al método que busca SÓLO los bonos de Tipo 1 con usos disponibles
        List<Bono> bonosValidos = bonoRepository.findBonosTipoUnoValidos(clienteId);

        // Comprobamos que tenga hueco en su bono de tipo 1
        if (bonosValidos.isEmpty()) {
            throw new RuntimeException("El cliente no tiene ningún Bono de Tipo 1 con usos disponibles.");
        }

        // Cogemos el primer bono válido de Tipo 1
        Bono bonoAUsar = bonosValidos.get(0);

        // Crear y guardar el Cupo
        Cupo cupo = new Cupo();
        cupo.setClase(clase);
        cupo.setCliente(cliente);
        cupo.setBono(bonoAUsar); // Se asigna y se consume siempre el bono de Tipo 1
        cupo.setEstado(Estado.CONFIRMADO);
        cupo.setFechaUso(LocalDateTime.now());

        cupoRepository.save(cupo);
    }

    public void removeCliente(Long claseId, Long clienteId) {
        Cupo cupo = cupoRepository.findByClienteIdAndClaseId(clienteId, claseId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la inscripción (cupo) de este cliente en la clase especificada."));
        cupoRepository.delete(cupo);
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