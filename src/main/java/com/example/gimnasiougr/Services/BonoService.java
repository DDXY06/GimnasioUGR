package com.example.gimnasiougr.Services;

import com.example.gimnasiougr.Models.Bono;
import com.example.gimnasiougr.Models.BonoDTO;
import com.example.gimnasiougr.Repositories.BonoRepository;
import com.example.gimnasiougr.Repositories.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BonoService {

    private final BonoRepository bonoRepository;
    private final ClienteRepository clienteRepository;

    public BonoService(BonoRepository bonoRepository, ClienteRepository clienteRepository) {
        this.bonoRepository = bonoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<BonoDTO> listarTodos() {
        List<BonoDTO> bonoDTOS = new ArrayList<>();
        for (Bono bono : bonoRepository.findAll()) {
            bonoDTOS.add(mapToDTO(bono));
        }
        return bonoDTOS;
    }

    @Transactional
    public Bono guardar(BonoDTO bonoDTO) {
        Bono bono = mapToEntity(bonoDTO);
        Bono guardado = bonoRepository.save(bono);
        return guardado;
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

    public List<BonoDTO> buscarPorFecha(LocalDate fecha) {
        if(fecha == null){
            return listarTodos();
        }else{
            List<BonoDTO> bonoDTOS = new ArrayList<>();
            for (Bono bono : bonoRepository.findByFechaCompra(fecha)) {
                bonoDTOS.add(mapToDTO(bono));
            }
            return bonoDTOS;
        }
    }



    public BonoDTO mapToDTO(Bono bono) {
        if (bono == null) return null;
        BonoDTO dto = new BonoDTO();
        dto.setId(bono.getId());
        if (bono.getCliente() != null) {
            dto.setUsuarioId(bono.getCliente().getId());
            dto.setNombreCliente(bono.getCliente().getNombre());
        }
        dto.setTipo(bono.getTipo());
        dto.setMaxCupos(bono.getMaxCupos());
        dto.setFechaCompra(bono.getFechaCompra());
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
        if (bonoDTO.getUsuarioId() != null) {
             bono.setCliente(clienteRepository.findById(bonoDTO.getUsuarioId()).orElse(null));
        }

        return bono;
    }
}
