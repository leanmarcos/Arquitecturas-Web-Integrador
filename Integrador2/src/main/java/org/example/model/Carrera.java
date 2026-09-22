package org.example.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "carrera")
public class Carrera {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "carrera")
    private List<EstudianteCarrera> estudiantes;
}
