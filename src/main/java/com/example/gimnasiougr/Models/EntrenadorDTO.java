package com.example.gimnasiougr.Models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntrenadorDTO {
    private Long id;
    private Long usuarioId; // Relación con el Usuario asociado

    @NotBlank(message = "Introduzca un nombre")
    @Size(max = 100, message = "Límite 100 caracteres")
    private String nombre;

    @NotBlank(message = "Introduzca un DNI")
    @Size(min = 9, max = 9, message = "Introduzca un documento válido")
    private String dni;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    private String telf;

    @NotBlank(message = "Introduzca un correo")
    @Email(message = "El formato del correo no es válido")
    private String correo;
    private String contrasenia;
}
