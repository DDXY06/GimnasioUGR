package com.example.gimnasiougr.Models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BonoDTO {
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    private String usuarioNombre;

    @NotNull(message = "El tipo de bono es obligatorio")
    private TipoClase tipo;

    @NotNull(message = "El número máximo de cupos es obligatorio")
    @Positive(message = "El número máximo de cupos debe ser positivo")
    private Integer maxCupos;

    @NotNull(message = "La fecha de compra es obligatoria")
    private LocalDate fechaCompra;
}
