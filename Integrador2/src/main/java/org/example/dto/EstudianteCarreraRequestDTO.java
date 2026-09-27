package org.example.dto;

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
        Integer dni,
        String nombreCarrera,
        Year anioInscripcion,
        Year anioGraduacion
) {
    public EstudianteCarreraRequestDTO {
        if (dni == null || dni <= 0) {
            throw new IllegalArgumentException("El DNI debe ser un número positivo.");
        }
        if (nombreCarrera == null || nombreCarrera.isBlank()) {
            throw new IllegalArgumentException("La carrera es obligatoria.");
        }
        if (anioInscripcion == null) {
            throw new IllegalArgumentException("El año de inscripción es obligatorio.");
        }
        if (anioGraduacion != null && anioGraduacion.isBefore(anioInscripcion)) {
            throw new IllegalArgumentException("El año de graduación no puede ser anterior al de inscripción.");
        }
    }
}
