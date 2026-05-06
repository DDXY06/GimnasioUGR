package com.example.gimnasiougr.Models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeporteDTO {
    private Long id;
    @NotBlank(message = "Introduzca un nombre")
    private String nombre;
}
