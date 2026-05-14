package com.example.gimnasiougr.Models;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaseDTO {
    private Long id;

    private List<CupoDTO> cuposDTO;

    @NotNull(message = "Introduzca un ID de deporte")
    private Long deporteId;

    private String nombreDeporte;

    @NotNull(message = "Introduzca un ID de entrenador")
    private Long entrenadorId;

    private String nombreEntrenador;

    @NotNull(message = "Introduzca un tipo de clase")
    @Enumerated(EnumType.STRING)
    private TipoClase tipo;

    @NotNull(message = "Introduzca un estado")
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Introduzca una fecha")
    private LocalDate fecha;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "Introduzca una hora")
    private LocalTime hora;

    private Integer maxCupos;

    private Integer cuposOcupados;
    private boolean inscrito;
    private Long cupoId;
    private boolean cambioSolicitado;
}