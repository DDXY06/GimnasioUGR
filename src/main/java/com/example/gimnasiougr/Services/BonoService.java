package com.example.gimnasiougr.Services;

import com.example.gimnasiougr.Models.Bono;
import com.example.gimnasiougr.Models.BonoDTO;
import com.example.gimnasiougr.Models.TipoBono;
import com.example.gimnasiougr.Repositories.BonoRepository;
import com.example.gimnasiougr.Repositories.ClienteRepository;
import com.example.gimnasiougr.Repositories.CupoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BonoService {

    private final BonoRepository bonoRepository;
    private final ClienteRepository clienteRepository;
    private final CupoService cupoService;

    public List<BonoDTO> listarTodos() {
        return bonoRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    public BonoDTO guardar(BonoDTO bonoDTO) {
        Bono bono = mapToEntity(bonoDTO);
        Bono guardado = bonoRepository.save(bono);
        return mapToDTO(guardado);
    }

    public BonoDTO buscarPorId(Long id) {
        return mapToDTO(bonoRepository.findById(id).orElse(null));
    }

    @Transactional
    public boolean eliminar(Long id) {
        if (bonoRepository.existsById(id)) {
            bonoRepository.findById(id).ifPresent(bono -> {
                bonoRepository.delete(bono);
            });
            return true;
        }
        return false;
    }

    public List<BonoDTO> buscarPorFiltro(String tipoFiltro, String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return listarTodos();
        }

        List<Bono> bonos;
        if ("cliente".equalsIgnoreCase(tipoFiltro)) {
            bonos = bonoRepository.findByClienteNombreContainingIgnoreCase(textoBusqueda);
        } else if ("tipo".equalsIgnoreCase(tipoFiltro)) {
            List<TipoBono> tiposCoincidentes = new ArrayList<>();
            for (TipoBono t : TipoBono.values()) {
                if (t.name().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                    tiposCoincidentes.add(t);
                }
            }
            if (tiposCoincidentes.isEmpty()) {
                bonos = new ArrayList<>();
            } else {
                bonos = bonoRepository.findByTipoIn(tiposCoincidentes);
            }
        } else {
            bonos = bonoRepository.findAll();
        }

        return bonos.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public BonoDTO mapToDTO(Bono bono) {
        if (bono == null) return null;
        BonoDTO dto = new BonoDTO();
        dto.setId(bono.getId());
        if (bono.getCliente() != null) {
            dto.setClienteId(bono.getCliente().getId());
            dto.setNombreCliente(bono.getCliente().getNombre());
        }
        dto.setTipo(bono.getTipo());
        dto.setMaxCupos(bono.getMaxCupos());
        dto.setFechaCompra(bono.getFechaCompra());
        dto.setCuposUsados((int) cupoService.contarPorBono(bono.getId()));
        return dto;
    }

    public Bono mapToEntity(BonoDTO bonoDTO) {
        Bono bono = new Bono();

        // Si el DTO trae un ID, significa que estamos editando un bono existente.
        // Lo buscamos en la base de datos para no perder sus datos.
        if (bonoDTO.getId() != null) {
            bono = bonoRepository.findById(bonoDTO.getId()).orElse(new Bono());
        }

        // Asignamos los campos que SÍ existen en tu modelo
        bono.setTipo(bonoDTO.getTipo());
        bono.setMaxCupos(bonoDTO.getMaxCupos());
        bono.setFechaCompra(bonoDTO.getFechaCompra());

        // Asignamos el cliente
        if (bonoDTO.getClienteId() != null) {
            bono.setCliente(clienteRepository.findById(bonoDTO.getClienteId()).orElse(null));
        }

        return bono;
    }

    public BonoDTO buscarDetalleConCupos(Long id) {
        return bonoRepository.findById(id)
                .map(bono -> {
                    BonoDTO dto = mapToDTO(bono);
                    dto.setCupos(cupoService.buscarPorBono(bono.getId()));
                    return dto;
                })
                .orElse(null);
    }

    public List<BonoDTO> buscarPorClienteId(Long clienteId) {
        return bonoRepository.findByClienteId(clienteId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}