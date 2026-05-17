package com.example.gimnasiougr.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Cupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Introduzca un usuario")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_Cliente", nullable = false)
    private Cliente cliente;

    @NotNull(message = "Introduzca una clase")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idClase", nullable = false)
    private Clase clase;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "idBono", nullable = true)
    private Bono bono;

    @NotNull(message = "Introduzca un estado")
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @NotNull(message = "Introduzca una fecha de uso")
    private LocalDateTime fechaUso;
}
