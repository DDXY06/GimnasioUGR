package com.example.gimnasiougr.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Introduzca un deporte")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDeporte")
    private Deporte deporte;

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL)
    List<Cupo> cupos;

    @NotNull(message = "Introduzca un entrenador")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEntrenador")
    private Entrenador entrenador;

    @NotNull(message = "Introduzca un tipo de clase")
    @Enumerated(EnumType.ORDINAL)
    private TipoClase tipo;

    @NotNull(message = "Introduzca un estado")
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Introduzca una fecha")
    private LocalDate fecha;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "Introduzca una hora")
    private LocalTime hora;

    @NotNull(message = "Introduzca un número máximo de cupos")
    private Integer maxCupos;
}
