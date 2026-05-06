package com.example.gimnasiougr.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Bono {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Introduzca un usuario")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "bono")
    List<Cupo> cuposUsados;

    @NotNull(message = "Introduzca un tipo de bono")
    @Enumerated(EnumType.ORDINAL)
    private TipoClase tipo;

    @NotNull(message = "Introduzca un número máximo de cupos")
    @Positive(message = "El número máximo de cupos debe ser positivo")
    private Integer maxCupos;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaCompra =  LocalDate.now();
}
