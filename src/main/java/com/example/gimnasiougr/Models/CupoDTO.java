package com.example.gimnasiougr.Models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CupoDTO {
    private Long id;

    @NotNull(message = "Introduzca un ID de usuario")
    private Long clienteId;

    @NotNull(message = "Introduzca un ID de clase")
    private Long claseId;

    @NotNull(message = "Introduzca unn ID de bono")
    private Long bonoId;

    @NotNull(message = "Introduzca un estado")
    private Estado estado;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime fechaUso;

    private String nombreDeporte;
    private String nombreEntrenador;
    private TipoClase tipoClase;
    private LocalDate fechaClase;
    private LocalTime horaClase;
    private Estado estadoClase;
}
