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


    public List<ClaseDTO> obtenerClasesTipo1ConEstado(Usuario usuario) {
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow();
        List<Clase> todasLasClases = claseRepository.findByTipo(TipoClase.UNO);
        List<ClaseDTO> resultados = new ArrayList<>();

        for (Clase clase : todasLasClases) {

            ClaseDTO dto = mapToClaseDTO(clase);

            // Buscamos si existe un Cupo de este cliente en esta clase
            Optional<Cupo> reserva = cupoRepository.findByUsuarioIdAndClaseId(cliente.getId(), clase.getId());

            // Si encontramos la reserva, marcamos el DTO como ture y guardamos el ID del cupo
            if (reserva.isPresent()) {
                dto.setInscrito(true);
                Long idCupo = reserva.get().getId();
                dto.setCupoId(idCupo);

                // Comprobamos si este cupo ya tiene una solicitud de cambio PENDIENTE
                boolean haySolicitud = solicitudCambioRepository.existsByCupoIdAndEstado(idCupo, Estado.PENDIENTE);
                dto.setCambioSolicitado(haySolicitud);

            } else {
                // Si no hay reserva, lo marcamos como no inscrito
                dto.setInscrito(false);
                dto.setCupoId(null);
                dto.setCambioSolicitado(false);
            }

            resultados.add(dto);
        }

        return resultados;
    }


    @Transactional
    public void procesarSolicitudCambio(Usuario usuario, Long cupoId, Long claseCambioId) {

        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow();
        Cupo cupoActual = cupoRepository.findById(cupoId).orElseThrow();
        Clase nuevaClase = claseRepository.findById(claseCambioId).orElseThrow();

        if (!cupoActual.getUsuario().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("Acceso denegado: el cupo no te pertenece.");
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


    private ClaseDTO mapToClaseDTO(Clase clase) {
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

        // Contamos solo los cupos confirmados para saber las plazas ocupadas
        int ocupados = 0;

        if (clase.getCupos() != null) {

            for (Cupo c : clase.getCupos()) {
                // Si el estado del cupo es CONFIRMADO, sumamos 1
                if (Estado.CONFIRMADO.equals(c.getEstado())) {
                    ocupados++;
                }
            }
        }
        dto.setCuposOcupados(ocupados);

        return dto;
    }

    private CupoDTO mapToCupoDTO(Cupo cupo) {
        CupoDTO dto = new CupoDTO();
        dto.setId(cupo.getId());
        dto.setEstado(cupo.getEstado());
        dto.setFechaUso(cupo.getFechaUso());

        if (cupo.getUsuario() != null) {
            dto.setClienteId(cupo.getUsuario().getId());
        }
        if (cupo.getClase() != null) {
            dto.setClaseId(cupo.getClase().getId());
            dto.setTipoClase(cupo.getClase().getTipo());
            dto.setFechaClase(cupo.getClase().getFecha());
            dto.setHoraClase(cupo.getClase().getHora());
            dto.setEstadoClase(cupo.getClase().getEstado());

            if (cupo.getClase().getDeporte() != null) {
                dto.setNombreDeporte(cupo.getClase().getDeporte().getNombre());
            }
            if (cupo.getClase().getEntrenador() != null) {
                dto.setNombreEntrenador(cupo.getClase().getEntrenador().getNombre());
            }
        }
        if (cupo.getBono() != null) {
            dto.setBonoId(cupo.getBono().getId());
        }

        return dto;
    }



    public List<CupoDTO> obtenerClasesTipo2(Usuario usuario) {
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow();
        List<Cupo> listaCuposOriginal = cupoRepository.findByUsuarioIdAndClaseTipo(cliente.getId(), TipoClase.DOS);
        List<CupoDTO> listadto = new ArrayList<>();

        for (Cupo cupo : listaCuposOriginal) {
            CupoDTO datosLimpios = this.mapToCupoDTO(cupo);

            listadto.add(datosLimpios);
        }

        return listadto;
    }

    @Transactional
    public void solicitarClaseTipo2(Usuario usuario, Long deporteId, Long entrenadorId,
                                    LocalDate fecha, LocalTime hora) {

        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow();
        boolean tieneBonoDos = bonoRepository.existsByClienteIdAndTipo(cliente.getId(), TipoBono.DOS);
        if (!tieneBonoDos) {
            throw new IllegalArgumentException("Necesitas un bono de Tipo 2 para solicitar clases individuales.");
        }


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
        cupo.setUsuario(cliente);
        cupo.setClase(nuevaClase);
        cupo.setEstado(Estado.PENDIENTE);
        cupo.setFechaUso(LocalDateTime.of(fecha, hora));

        cupoRepository.save(cupo);
    }

    public List<ClaseDTO> obtenerClasesTipo3ConEstado(Usuario usuario) {
        // Buscamos al cliente asociado al usuario logeado
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow();

        // Traemos todas las clases de tipo 3
        List<Clase> clasesTipo3 = claseRepository.findByTipo(TipoClase.TRES);
        List<ClaseDTO> resultados = new ArrayList<>();

        for (Clase clase : clasesTipo3) {
            ClaseDTO dto = new ClaseDTO();
            dto.setId(clase.getId());
            dto.setNombreDeporte(clase.getDeporte().getNombre());
            dto.setNombreEntrenador(clase.getEntrenador().getNombre());
            dto.setFecha(clase.getFecha());
            dto.setHora(clase.getHora());
            dto.setMaxCupos(clase.getMaxCupos());

            // Calculamos cuánta gente hay apuntada actualmente
            dto.setCuposOcupados((int) cupoRepository.countByClase(clase));

            // Comprobamos si EL CLIENTE ACTUAL ya está en esta clase
            boolean yaInscrito = cupoRepository.existsByUsuarioAndClase(cliente, clase);
            dto.setInscrito(yaInscrito);

            resultados.add(dto);
        }
        return resultados;
    }

    @Transactional
    public void inscribirClaseTipo3(Usuario usuario, Long claseId) {
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Clase clase = claseRepository.findById(claseId).orElseThrow(() -> new RuntimeException("La clase no existe"));


        if (cupoRepository.existsByUsuarioAndClase(cliente, clase)) {
            throw new RuntimeException("Ya estás inscrito en esta clase");
        }

        long inscritos = cupoRepository.countByClase(clase);
        if (inscritos >= clase.getMaxCupos()) {
            throw new RuntimeException("Lo sentimos, la clase está completa");
        }

        // Traemos todos los bonos del cliente
        List<Bono> todosMisBonos = bonoRepository.findByClienteId(cliente.getId());
        Bono bonoAElegir = null;

        for (Bono bono : todosMisBonos) {
            long usados = cupoRepository.countByBono(bono);

            // Si los usados son menos que el máximo, este bono nos sirve
            if (usados < bono.getMaxCupos()) {
                bonoAElegir = bono;
                break;
            }
        }
        // Si después del bucle no hemos encontrado nada, lanzamos error
        if (bonoAElegir == null) {
            throw new RuntimeException("No tienes bonos con cupos disponibles");
        }

        // Crear la inscripción (Cupo)
        Cupo nuevoCupo = new Cupo();
        nuevoCupo.setUsuario(cliente);
        nuevoCupo.setClase(clase);
        nuevoCupo.setEstado(Estado.CONFIRMADO);
        nuevoCupo.setFechaUso(LocalDateTime.now());

        cupoRepository.save(nuevoCupo);
    }
}