package com.example.gimnasiougr.Services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gimnasiougr.Models.Clase;
import com.example.gimnasiougr.Models.Cliente;
import com.example.gimnasiougr.Models.Cupo;
import com.example.gimnasiougr.Models.Estado;
import com.example.gimnasiougr.Models.SolicitudCambio;
import com.example.gimnasiougr.Models.SolicitudCambioDTO;
import com.example.gimnasiougr.Repositories.ClaseRepository;
import com.example.gimnasiougr.Repositories.ClienteRepository;
import com.example.gimnasiougr.Repositories.CupoRepository;
import com.example.gimnasiougr.Repositories.SolicitudCambioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolCambioService {
    private final SolicitudCambioRepository solicitudCambioRepository;
    private final ClienteRepository clienteRepository;
    private final CupoRepository cupoRepository;
    private final ClaseRepository claseRepository;

    public List<SolicitudCambioDTO> listarCambios() {
        return solicitudCambioRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public SolicitudCambioDTO buscarPorId(Long id) {
        return mapToDTO(solicitudCambioRepository.findById(id).orElse(null));
    }

    @Transactional
    public void aceptarCambio(SolicitudCambioDTO dto) {
        SolicitudCambio solicitud = mapToEntity(dto);
        if (solicitud != null && solicitud.getCupo() != null && solicitud.getClaseCambio() != null) {
            Cupo cupo = solicitud.getCupo();
            cupo.setClase(solicitud.getClaseCambio());
            cupo.setEstado(Estado.CONFIRMADO);
            cupoRepository.save(cupo);

            solicitud.setEstado(Estado.CONFIRMADO);
            solicitudCambioRepository.save(solicitud);
        }
    }

    @Transactional
    public void rechazarCambio(SolicitudCambioDTO dto) {
        SolicitudCambio solicitud = mapToEntity(dto);
        if (solicitud != null) {
            solicitud.setEstado(Estado.CANCELADO);
            solicitudCambioRepository.save(solicitud);
        }
    }
    public SolicitudCambioDTO mapToDTO(SolicitudCambio solCambio) {
        if (solCambio == null) {
            return null;
        }

        SolicitudCambioDTO dto = new SolicitudCambioDTO();
        dto.setId(solCambio.getId());
        
        if (solCambio.getCliente() != null) {
            dto.setClienteId(solCambio.getCliente().getId());
            dto.setUsuarioNombre(solCambio.getCliente().getNombre());
        }
        
        if (solCambio.getCupo() != null) {
            dto.setCupoId(solCambio.getCupo().getId());
            if (solCambio.getCupo().getClase() != null) {
                dto.setFechaClaseOriginal(solCambio.getCupo().getClase().getFecha());
            }
        }
        
        if (solCambio.getClaseCambio() != null) {
            dto.setClaseCambioId(solCambio.getClaseCambio().getId());
            dto.setFechaClaseCambio(solCambio.getClaseCambio().getFecha());
        }
        
        dto.setEstado(solCambio.getEstado());
        dto.setFechaSolicitud(solCambio.getFechaSolicitud());
        
        return dto;
    }

    public SolicitudCambio mapToEntity(SolicitudCambioDTO dto) {
        if (dto == null) {
            return null;
        }

        SolicitudCambio solCambio = new SolicitudCambio();
        
        if (dto.getId() != null) {
            solCambio = solicitudCambioRepository.findById(dto.getId()).orElse(new SolicitudCambio());
        }

        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId()).orElse(null);
            solCambio.setCliente(cliente);
        }

        if (dto.getCupoId() != null) {
            Cupo cupo = cupoRepository.findById(dto.getCupoId()).orElse(null);
            solCambio.setCupo(cupo);
        }

        if (dto.getClaseCambioId() != null) {
            Clase clase = claseRepository.findById(dto.getClaseCambioId()).orElse(null);
            solCambio.setClaseCambio(clase);
        }

        solCambio.setEstado(dto.getEstado());
        solCambio.setFechaSolicitud(dto.getFechaSolicitud());

        return solCambio;
    }
}
