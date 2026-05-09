package com.example.gimnasiougr.Models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Long id;
    private Long usuarioId; // Relación con el Usuario asociado

    @NotBlank(message = "Introduzca un nombre")
    private String nombre;

    @NotBlank(message = "Introduzca un DNI")
    private String dni;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    private String telf;

    @NotBlank(message = "Introduzca un correo")
    @Email(message = "El formato del correo no es válido")
    private String correo;

    private String direccion;

    private String contrasenia;

    private List<BonoDTO> bonos; // Relación con Bonos
}
