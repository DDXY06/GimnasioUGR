package com.example.gimnasiougr.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull(message = "El deporte es obligatorio")
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDeporte", nullable = false)
    private Deporte deporte;

    @NotNull(message = "El entrenador es obligatorio")
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEntrenador", nullable = false)
    private Entrenador entrenador;

    @NotNull(message = "El tipo de clase es obligatorio")
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private TipoClase tipo;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Estado estado;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    @Column(nullable = false)
    private LocalTime hora;

    @Column(name = "maxCupos")
    private Integer maxCupos;
}
