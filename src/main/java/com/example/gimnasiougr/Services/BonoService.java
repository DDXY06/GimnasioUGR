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
        List<Bono> bonos = bonoRepository.findAll();

        // Preparamos una lista vacía para guardar los DTOs
        List<BonoDTO> listaDtos = new ArrayList<>();

        for (Bono bono : bonos) {
            BonoDTO dto = mapToDTO(bono);
            listaDtos.add(dto);
        }

        return listaDtos;
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
            bonoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<BonoDTO> buscarPorFiltro(String tipoFiltro, String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.isBlank()) {
            return listarTodos();
        }

        // Definimos la lista que guardará lo que encontremos en la base de datos
        List<Bono> bonos;
        String filtro = tipoFiltro.toLowerCase();

        // Buscamos según el filtro seleccionado
        switch (filtro) {
            case "cliente":
                bonos = bonoRepository.findByClienteNombreContainingIgnoreCase(textoBusqueda);
                break;

            case "tipo":
                TipoBono tipoCoincidente = null;
                // Buscamos el PRIMER tipo de bono que encaje con el texto
                for (TipoBono t : TipoBono.values()) {
                    if (t.name().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                        tipoCoincidente = t;
                        break;
                    }
                }

                // Si encontramos un tipo, buscamos sus bonos. Si no, lista vacía.
                if (tipoCoincidente != null) {
                    bonos = bonoRepository.findByTipo(tipoCoincidente);
                } else {
                    bonos = new ArrayList<>();
                }
                break;

            default:
                bonos = bonoRepository.findAll();
                break;
        }

        // Pasamos la lista
        List<BonoDTO> listaBonoDto = new ArrayList<>();
        for (Bono b : bonos) {
            BonoDTO dto = mapToDTO(b);
            listaBonoDto.add(dto);
        }

        return listaBonoDto;
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
        Bono bono = bonoRepository.findById(id).orElse(null);

        if (bono == null) {
            return null;
        }

        // Si existe, lo transformamos a DTO
        BonoDTO bonoDto = mapToDTO(bono);

        // Le añadimos los cupos
        bonoDto.setCupos(cupoService.buscarPorBono(bono.getId()));

        return bonoDto;
    }

    public List<BonoDTO> buscarPorClienteId(Long clienteId) {
        List<Bono> bonosDelCliente = bonoRepository.findByClienteId(clienteId);

        // Creamos la lista vacía donde guardaremos los DTOs
        List<BonoDTO> listaResultado = new ArrayList<>();

        for (Bono bono : bonosDelCliente) {
            BonoDTO dto = mapToDTO(bono);
            listaResultado.add(dto);
        }

        return listaResultado;
    }
}