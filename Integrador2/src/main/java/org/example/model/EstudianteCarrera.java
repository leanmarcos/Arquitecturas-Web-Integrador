package org.example.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import java.time.LocalDate;

@Entity()
@Table(name = "estudiante_carrera",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_estudiante", "id_carrera"}))
@Check(constraints =
        "((graduado = TRUE AND fecha_graduacion IS NOT NULL) OR (graduado = FALSE AND fecha_graduacion IS NULL))" +
        " AND (fecha_graduacion IS NULL OR fecha_graduacion >= fecha_inscripcion)")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EstudianteCarrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera", nullable = false)
    private Carrera carrera;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion;

    @Column(name = "fecha_graduacion" , nullable = true)
    private LocalDate fechaGraduacion;

    @Column(name = "graduado" , nullable = false)
    private boolean graduado;

    @Builder
    public EstudianteCarrera(Estudiante estudiante, Carrera carrera, LocalDate fechaInscripcion) {
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.fechaInscripcion = fechaInscripcion;
    }
}
