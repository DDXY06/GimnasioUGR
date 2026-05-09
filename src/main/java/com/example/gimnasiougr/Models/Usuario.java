package com.example.gimnasiougr.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Introduzca un correo")
    @Email(message = "El formato del correo no es válido")
    private String correo;

    @NotBlank(message = "Introduzca una contraseña")
    private String contrasenia;

    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.ORDINAL)
    private TipoUsuario rol;
}
