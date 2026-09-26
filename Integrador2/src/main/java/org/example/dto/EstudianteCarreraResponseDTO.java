package org.example.dto;

import lombok.Builder;

import java.time.Year;

@Builder
public record EstudianteCarreraResponseDTO(
        Long id,
        Long luEstudiante,
        Long idCarrera,
        String nombreCarrera,
        Year anioInscripcion,
        Year anioGraduacion,
        boolean graduado
) {
}
