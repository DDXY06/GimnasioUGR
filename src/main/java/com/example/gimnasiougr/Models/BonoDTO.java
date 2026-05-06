package com.example.gimnasiougr.Models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BonoDTO {
    private Long id;

    @NotNull(message = "Introduzca un ID de usuario")
    private Long usuarioId;

    private String usuarioNombre;

    @NotNull(message = "Introduzca un tipo de bono")
    private TipoClase tipo;

    @NotNull(message = "Introduzca un número máximo de cupos")
    @Positive(message = "El número máximo de cupos debe ser positivo")
    private Integer maxCupos;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Introduzca una fecha de compra")
    private LocalDate fechaCompra =   LocalDate.now();
}
