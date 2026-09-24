package org.example.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EstudianteCarreraResponseDTO(
        Long id,
        Long luEstudiante,
        Long idCarrera,
        String nombreCarrera,
        LocalDate fechaInscripcion,
        LocalDate fechaGraduacion,
        boolean graduado
) {
}
