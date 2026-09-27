package org.example.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import java.time.Year;

@Entity()
@Table(name = "estudiante_carrera",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_estudiante", "id_carrera"}))
@Check(constraints =
        "((graduado = TRUE AND anio_graduacion IS NOT NULL) OR (graduado = FALSE AND anio_graduacion IS NULL))" +
        " AND (anio_graduacion IS NULL OR anio_graduacion >= anio_inscripcion)")
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

    @Column(name = "anio_inscripcion", nullable = false)
    private Year anioInscripcion;

    @Column(name = "anio_graduacion" , nullable = true)
    private Year anioGraduacion;

    @Column(name = "graduado" , nullable = false)
    private boolean graduado;

    @Builder
    public EstudianteCarrera(Estudiante estudiante, Carrera carrera, Year anioInscripcion,
                             Year anioGraduacion) {
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.anioInscripcion = anioInscripcion;
        this.anioGraduacion = anioGraduacion;
        this.graduado = anioGraduacion != null;
    }
}
