package org.example.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record EstudiantesPorCarreraResponseDTO(
        String carrera,
        String ciudad,
        List<EstudianteResponseDTO> estudiantes) {
}
