package org.example.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "estudiante")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Estudiante {

    @Id
    private Long lu;

    @Column(unique = true, nullable = false)
    private Integer dni;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private Integer edad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstudianteGenero genero;

    @Column(name = "ciudad_residencia", nullable = false)
    private String ciudadResidencia;

    @OneToMany(mappedBy = "estudiante")
    private List<EstudianteCarrera> inscripciones;

}
