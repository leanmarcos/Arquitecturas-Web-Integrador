package org.example.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.example.model.EstudianteCarrera;

import java.time.Year;

/**
 * {@link EstudianteCarrera}. El estudiante no viaja en el body: se toma de la LU del path.
 *
 * @param anioGraduacion {@code null} si el estudiante no se graduó
 * @param antiguedad     {@code null} para que se calcule a partir de los años
 */
@Builder
public record EstudianteCarreraRequestDTO(
        @NotNull(message = "El id de la carrera es obligatorio")
        @Positive(message = "El id de la carrera debe ser mayor a 0")
        Long idCarrera,

        @NotNull(message = "El año de inscripción es obligatorio")
        Year anioInscripcion,

        Year anioGraduacion,

        @PositiveOrZero(message = "La antigüedad no puede ser negativa")
        Integer antiguedad) {

    @AssertTrue(message = "El año de graduación no puede ser anterior al de inscripción")
    public boolean isGraduacionValida() {
        return anioGraduacion == null || anioInscripcion == null || !anioGraduacion.isBefore(anioInscripcion);
    }
}
