package org.example.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.example.model.EstudianteCarrera;

import java.time.Year;

/**
 * {@link EstudianteCarrera}
 *
 * @param anioGraduacion {@code null} si el estudiante no se graduó
 */
@Builder
public record EstudianteCarreraRequestDTO(
        @NotNull(message = "El DNI del estudiante es obligatorio")
        @Positive(message = "El DNI debe ser un número positivo")
        Integer dni,

        @NotBlank(message = "El nombre de la carrera es obligatorio")
        String nombreCarrera,

        @NotNull(message = "El año de inscripción es obligatorio")
        Year anioInscripcion,

        Year anioGraduacion) {

    @AssertTrue(message = "El año de graduación no puede ser anterior al de inscripción")
    public boolean isGraduacionValida() {
        return anioGraduacion == null || anioInscripcion == null || !anioGraduacion.isBefore(anioInscripcion);
    }
}
