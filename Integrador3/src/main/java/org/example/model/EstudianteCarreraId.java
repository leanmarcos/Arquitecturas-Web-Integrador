package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * Clave compuesta de {@link EstudianteCarrera}: un estudiante se inscribe una sola vez en cada carrera.
 * <p>
 * JPA exige que una clave embebida sea {@code Serializable} y defina {@code equals} / {@code hashCode}, porque la usa
 * para identificar la entidad en el contexto de persistencia.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EstudianteCarreraId implements Serializable {

    @Column(name = "id_estudiante")
    private Long idEstudiante;

    @Column(name = "id_carrera")
    private Long idCarrera;
}
