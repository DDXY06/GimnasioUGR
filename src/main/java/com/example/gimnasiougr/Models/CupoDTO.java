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

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    private String usuarioNombre;

    @NotNull(message = "El ID de la clase es obligatorio")
    private Long claseId;

    private Long bonoId;

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    private LocalDateTime fechaUso;
}
