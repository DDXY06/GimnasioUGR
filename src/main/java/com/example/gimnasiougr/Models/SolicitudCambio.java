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
public class SolicitudCambio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Introduzca un usuario")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @NotNull(message = "Introduzca un cupo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCupo", nullable = false)
    private Cupo cupo;

    @NotNull(message = "Introduzca una clase de cambio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idClaseCambio", nullable = false)
    private Clase claseCambio;

    @NotNull(message = "Introduzca un estado")
    @Enumerated(EnumType.STRING)
    private Estado estado;

    private LocalDateTime fechaSolicitud;
}
