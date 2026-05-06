package com.example.gimnasiougr.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(max = 20, message = "El DNI no puede exceder los 20 caracteres")
    @Column(nullable = false, unique = true, length = 20)
    private String dni;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    @Column(length = 20)
    private String telf;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El formato del correo no es válido")
    @Size(max = 100, message = "El correo no puede exceder los 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres")
    @Column(length = 255)
    private String direccion;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(max = 255, message = "La contraseña no puede exceder los 255 caracteres")
    @Column(nullable = false, length = 255)
    private String contrasenia;
}
