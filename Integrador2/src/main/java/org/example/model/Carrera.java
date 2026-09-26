package org.example.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "carrera")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Carrera {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "duracion", nullable = false)
    private Integer duracion;

    @OneToMany(mappedBy = "carrera")
    private List<EstudianteCarrera> estudiantes;

    @Builder
    public Carrera(String nombre, Integer duracion) {
        this.nombre = nombre;
        this.duracion = duracion;
    }
}
