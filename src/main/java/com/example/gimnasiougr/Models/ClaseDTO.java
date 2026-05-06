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

    @NotNull(message = "El ID del deporte es obligatorio")
    private Long deporteId;

    private String deporteNombre;

    @NotNull(message = "El ID del entrenador es obligatorio")
    private Long entrenadorId;

    private String entrenadorNombre;

    @NotNull(message = "El tipo de clase es obligatorio")
    private TipoClase tipo;

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    private Integer maxCupos;
}
