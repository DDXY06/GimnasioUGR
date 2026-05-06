package com.example.gimnasiougr.Models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaseDTO {
    private Long id;

    @NotNull(message = "Introduzca un ID de deporte")
    private Long deporteId;

    private String deporteNombre;

    @NotNull(message = "Introduzca un ID de entrenador")
    private Long entrenadorId;

    private String entrenadorNombre;

    @NotNull(message = "Introduzca un tipo de clase")
    private TipoClase tipo;

    @NotNull(message = "Introduzca un estado")
    private Estado estado;

    @NotNull(message = "Introduzca una fecha")
    private LocalDate fecha;

    @NotNull(message = "Introduzca una hora")
    private LocalTime hora;

    @NotNull(message = "Introduzca un número máximo de cupos")
    private Integer maxCupos;
}
