package com.example.gimnasiougr.Services;

import com.example.gimnasiougr.Models.*;
import com.example.gimnasiougr.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteClasesService {

    private final ClienteRepository clienteRepository;
    private final CupoRepository cupoRepository;
    private final ClaseRepository claseRepository;
    private final SolicitudCambioRepository solicitudCambioRepository;
    private final DeporteRepository deporteRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final BonoRepository bonoRepository;
    private final ClaseService claseService;
    private final CupoService cupoService;


    /**
     * CLASES TIPO 1
     */

    public List<ClaseDTO> obtenerClasesTipo1ConEstado(Long idUsusio) {
        Cliente cliente = clienteRepository.findByUsuarioId(idUsusio)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene un perfil de cliente asociado."));

        List<Clase> todasLasClases = claseRepository.findByTipo(TipoClase.UNO);
        List<ClaseDTO> resultados = new ArrayList<>();

        for (Clase clase : todasLasClases) {
            ClaseDTO claseDTO = claseService.mapToDTO(clase);

            // Hacemos una consulta si este cliente concreto está apuntado a esta clase concreta
            Optional<Cupo> cupoOptional = cupoRepository.findByClienteIdAndClaseId(cliente.getId(), clase.getId());

            // Si está apuntado en esta clase añadimos que si está inscrito en el DTO y le añadimos el ID del cupo al que pertenece
            if (cupoOptional.isPresent()) {
                Cupo cupo = cupoOptional.get();
                claseDTO.setInscrito(true);
                claseDTO.setCupoId(cupo.getId());

                // Comprobamos si existe alguna solicitud de cambio para este Cupo
                boolean haySolicitud = solicitudCambioRepository.existsByCupoIdAndEstado(cupo.getId(), Estado.PENDIENTE);
                claseDTO.setCambioSolicitado(haySolicitud);
            } else {
                claseDTO.setInscrito(false);
                claseDTO.setCupoId(null);
                claseDTO.setCambioSolicitado(false);
            }

            resultados.add(claseDTO);
        }

        return resultados;
    }


    @Transactional
    public void procesarSolicitudCambioClaseTipo1(Long idUsuario, Long cupoId, Long claseCambioId) {

        Cliente cliente = clienteRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene un perfil de cliente asociado."));

        Cupo cupoActual = cupoRepository.findById(cupoId)
                .orElseThrow(() -> new IllegalArgumentException("El cupo especificado no existe o ya no está disponible."));

        Clase nuevaClase = claseRepository.findById(claseCambioId)
                .orElseThrow(() -> new IllegalArgumentException("La clase a la que intentas cambiar no existe."));

        // Validar que el cupo realmente pertenece al cliente que hace la petición
        if (!cupoActual.getCliente().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("Operación no permitida: Este cupo no te pertenece.");
        }

        boolean yaExisteSolicitud = solicitudCambioRepository.existsByCupoIdAndEstado(cupoId, Estado.PENDIENTE);
        if (yaExisteSolicitud) {
            throw new IllegalArgumentException("Ya tienes una solicitud de cambio pendiente para esta clase.");
        }

        if (cupoActual.getClase().getId().equals(claseCambioId)) {
            throw new IllegalArgumentException("La clase de destino debe ser diferente a la actual.");
        }

        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setCliente(cliente);
        solicitud.setCupo(cupoActual);
        solicitud.setClaseCambio(nuevaClase);
        solicitud.setEstado(Estado.PENDIENTE);
        solicitud.setFechaSolicitud(LocalDateTime.now());

        solicitudCambioRepository.save(solicitud);
    }

    /**
     * CLASES TIPO 2
     */

    public List<CupoDTO> obtenerClasesTipo2(Long idUsuario) {
        Cliente cliente = clienteRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene un perfil de cliente asociado."));

        List<Cupo> listaCuposOriginal = cupoRepository.findByClienteIdAndClaseTipo(cliente.getId(), TipoClase.DOS);
        List<CupoDTO> listaCuposDTO = new ArrayList<>();

        for (Cupo cupo : listaCuposOriginal) {
            CupoDTO datosLimpios = cupoService.mapToDTO(cupo);

            listaCuposDTO.add(datosLimpios);
        }

        return listaCuposDTO;
    }

    @Transactional
    public void solicitarClaseTipo2(Long idUsuario, Long deporteId, Long entrenadorId, LocalDate fecha, LocalTime hora) {

        Cliente cliente = clienteRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene un perfil de cliente asociado."));
        Bono bonoDos = bonoRepository.findFirstByClienteIdAndTipo(cliente.getId(), TipoBono.DOS)
                .orElseThrow(() -> new IllegalArgumentException("Necesitas un bono de Tipo 2 para solicitar clases individuales."));



        Clase nuevaClase = new Clase();
        nuevaClase.setDeporte(deporteRepository.findById(deporteId).orElseThrow());
        nuevaClase.setEntrenador(entrenadorRepository.findById(entrenadorId).orElseThrow());
        nuevaClase.setTipo(TipoClase.DOS);
        nuevaClase.setEstado(Estado.PENDIENTE);
        nuevaClase.setFecha(fecha);
        nuevaClase.setHora(hora);
        nuevaClase.setMaxCupos(1);

        claseRepository.save(nuevaClase);

        Cupo cupo = new Cupo();
        cupo.setCliente(cliente);
        cupo.setClase(nuevaClase);
        cupo.setBono(bonoDos);
        cupo.setEstado(Estado.PENDIENTE);
        cupo.setFechaUso(LocalDateTime.of(fecha, hora));

        cupoRepository.save(cupo);
    }

    /**
     * CLASES TIPO 3
     */
    public List<ClaseDTO> obtenerClasesTipo3ConEstado(Long idUsuario) {
        Cliente cliente = clienteRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene un perfil de cliente asociado."));

        List<Clase> litaClases = claseRepository.findByTipo(TipoClase.TRES);
        List<ClaseDTO> listaClasesDTO = new ArrayList<>();

        for (Clase clase : litaClases) {

            ClaseDTO ClaseDTO = claseService.mapToDTO(clase);

            // Añadimos los la cantidad de personas apuntadas a la clase
            ClaseDTO.setCuposOcupados( (int) cupoRepository.countByClaseId(clase.getId()));

            // Comprobamos si ya está inscrito
            boolean yaInscrito = cupoRepository.existsByClienteIdAndClaseId(cliente.getId(), clase.getId());
            ClaseDTO.setInscrito(yaInscrito);

            listaClasesDTO.add(ClaseDTO);
        }
        return listaClasesDTO;
    }

    @Transactional
    public void inscribirClaseTipo3(Long idUsuario, Long claseId) {
        Cliente cliente = clienteRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene un perfil de cliente asociado."));

        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new IllegalArgumentException("La clase seleccionada no existe."));

        // Comprueba que el cliente no esté inscrito en la clase ya
        if (cupoRepository.existsByClienteIdAndClaseId(cliente.getId(), clase.getId())) {
            throw new IllegalArgumentException("Ya estás inscrito en esta clase.");
        }
        // Comprobamos que la clase no este llena
        long inscritos = cupoRepository.countByClaseId(clase.getId());
        if (inscritos >= clase.getMaxCupos()) {
            throw new IllegalArgumentException("Lo sentimos, la clase está completa.");
        }

        // Guardamos la lista de bonos del cliente
        List<Bono> litaBonosCliente = bonoRepository.findByClienteId(cliente.getId());
        Bono bonoAElegir = null;
        // Comprobamos que algún bono de los que tiene tenga cupos disponibles
        for (Bono bono : litaBonosCliente) {
            long usados = cupoRepository.countByBonoId(bono.getId());

            if (usados < bono.getMaxCupos()) {
                bonoAElegir = bono;
                break;
            }
        }

        // Si no tiene ningún cupo, pues lanza excepción
        if (bonoAElegir == null) {
            throw new IllegalArgumentException("No tienes bonos con cupos disponibles.");
        }

        // Si ha llegado aquí significa que si tiene algún cupo disponible de algún bono
        // Creo un cupo porque no le puedo gastar cupos
        Cupo nuevoCupo = new Cupo();
        nuevoCupo.setCliente(cliente);
        nuevoCupo.setClase(clase);
        nuevoCupo.setEstado(Estado.CONFIRMADO);
        nuevoCupo.setFechaUso(LocalDateTime.now());

        cupoRepository.save(nuevoCupo);
    }
}