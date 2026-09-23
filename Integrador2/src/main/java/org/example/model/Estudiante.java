package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "estudiante")
@Getter
@Setter
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lu;

    @Column(unique = true, nullable = false)
    private Integer dni;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellido;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private String genero;

    @Column(name = "ciudad_residencia", nullable = false)
    private String ciudadResidencia;

    @OneToMany(mappedBy = "estudiante")
    private List<EstudianteCarrera> inscripciones;
}
