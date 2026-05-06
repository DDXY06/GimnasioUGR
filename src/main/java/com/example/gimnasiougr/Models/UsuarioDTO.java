package com.example.gimnasiougr.Models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(max = 8, message = "El DNI no puede exceder los 8 caracteres")
    private String dni;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    private String telf;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El formato del correo no es válido")
    @Size(max = 100, message = "El correo no puede exceder los 100 caracteres")
    private String correo;

    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres")
    private String direccion;
}
