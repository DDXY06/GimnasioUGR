package com.example.gimnasiougr.Models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CupoDTO {
    private Long id;

    @NotNull(message = "Introduzca un ID de usuario")
    private Long usuarioId;

    private String usuarioNombre;

    @NotNull(message = "Introduzca un ID de clase")
    private Long claseId;

    private Long bonoId;

    @NotNull(message = "Introduzca un estado")
    private Estado estado;

    private LocalDateTime fechaUso;
}
