package com.example.gimnasiougr.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Bono {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @NotNull(message = "El tipo de bono es obligatorio")
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private TipoClase tipo;

    @NotNull(message = "El número máximo de cupos es obligatorio")
    @Positive(message = "El número máximo de cupos debe ser positivo")
    @Column(name = "maxCupos", nullable = false)
    private Integer maxCupos;

    @NotNull(message = "La fecha de compra es obligatoria")
    @Column(name = "fechaCompra", nullable = false)
    private LocalDate fechaCompra;
}
