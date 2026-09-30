package org.example.dto;

import lombok.Builder;

import java.time.Year;

@Builder
public record EstudianteCarreraResponseDTO(
        Long luEstudiante,
        Long idCarrera,
        String nombreCarrera,
        Year anioInscripcion,
        Year anioGraduacion,
        boolean graduado
) {
    @Override
    public String toString() {
        String graduacion = graduado ? "graduado en " + anioGraduacion : "sin graduar";
        return String.format("LU %d en %s | inscripto en %s | %s",
                luEstudiante, nombreCarrera, anioInscripcion, graduacion);
    }
}
