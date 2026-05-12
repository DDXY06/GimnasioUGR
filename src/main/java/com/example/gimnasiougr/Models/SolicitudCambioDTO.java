package com.example.gimnasiougr.Models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudCambioDTO {
    private Long id;

    @NotNull(message = "Introduzca un ID de usuario")
    private Long clienteId;

    private String usuarioNombre;

    @NotNull(message = "Introduzca un ID de cupo")
    private Long cupoId;

    @NotNull(message = "Introduzca un ID de clase de cambio")
    private Long claseCambioId;

    @NotNull(message = "Introduzca un estado")
    private Estado estado;

    private LocalDateTime fechaSolicitud;
}
