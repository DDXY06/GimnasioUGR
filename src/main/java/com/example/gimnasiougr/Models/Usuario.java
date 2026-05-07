package com.example.gimnasiougr.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    List<Cupo> cupos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    List<Bono> bonos;

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

    @NotBlank(message = "Introduzca una contraseña")
    private String contrasenia;
}
