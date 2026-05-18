package com.example.gimnasiougr.Services;

import com.example.gimnasiougr.Models.*;
import com.example.gimnasiougr.Repositories.BonoRepository;
import com.example.gimnasiougr.Repositories.CupoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CupoService {

    private final CupoRepository cupoRepository;
    private final BonoRepository bonoRepository;

    public long contarPorBono(Long bonoId) {
        return cupoRepository.countByBonoId(bonoId);
    }

    public List<CupoDTO> buscarPorBono(Long bonoId) {
        List<Cupo> listaCupos = cupoRepository.findByBonoId(bonoId);
        List<CupoDTO> listaResultadoDTO = new ArrayList<>();

        for (Cupo cupo : listaCupos) {
            CupoDTO dto = this.mapToDTO(cupo);
            listaResultadoDTO.add(dto);
        }
        return listaResultadoDTO;
    }

    public List<CupoDTO> buscarPorIdClase(Long claseId){
        List<Cupo> listarCupos = cupoRepository.findByClaseId(claseId);
        List<CupoDTO> listarCuposDTO = new ArrayList<>();

        for (Cupo cupo : listarCupos) {
            CupoDTO dto = this.mapToDTO(cupo);
            listarCuposDTO.add(dto);
        }
        return listarCuposDTO;

    }

    @Transactional
    public void aceptarCupoTipo2(Cupo cupo){
        List<Bono> bonos = bonoRepository.findByTipoOrderByFechaCompraAsc(TipoBono.DOS);
        for (Bono bono : bonos) {
            long cuposUsados = cupoRepository.countByBonoId(bono.getId());
            if (cuposUsados < bono.getMaxCupos()) {
                cupo.setBono(bono);
                cupo.setEstado(Estado.CONFIRMADO);
                cupoRepository.save(cupo);
                break;
            }
        }
    }

    @Transactional
    public void rechazarCupoTipo2(Cupo cupo){
        cupo.setEstado(Estado.CANCELADO);
        cupoRepository.save(cupo);
    }

    public CupoDTO mapToDTO(Cupo cupo) {
        if (cupo == null) return null;

        CupoDTO dto = new CupoDTO();
        dto.setId(cupo.getId());

        dto.setEstado(cupo.getEstado());
        dto.setFechaUso(cupo.getFechaUso());

        if (cupo.getCliente() != null) {
            dto.setClienteId(cupo.getCliente().getId());
        }

        if (cupo.getClase() != null) {
            dto.setClaseId(cupo.getClase().getId());
            dto.setTipoClase(cupo.getClase().getTipo());
            dto.setFechaClase(cupo.getClase().getFecha());
            dto.setHoraClase(cupo.getClase().getHora());

            dto.setEstadoClase(cupo.getClase().getEstado());
            if (cupo.getClase().getEntrenador() != null) {
                dto.setNombreEntrenador(cupo.getClase().getEntrenador().getNombre());
            }

            if (cupo.getClase().getDeporte() != null) {
                dto.setNombreDeporte(cupo.getClase().getDeporte().getNombre());
            }
        }

        if (cupo.getBono() != null) {
            dto.setBonoId(cupo.getBono().getId());
        }

        return dto;
    }
}