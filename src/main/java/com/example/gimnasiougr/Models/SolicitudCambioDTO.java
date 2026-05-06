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

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    private String usuarioNombre;

    @NotNull(message = "El ID del cupo es obligatorio")
    private Long cupoId;

    @NotNull(message = "El ID de la clase de cambio es obligatorio")
    private Long claseCambioId;

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    private LocalDateTime fechaSolicitud;
}
