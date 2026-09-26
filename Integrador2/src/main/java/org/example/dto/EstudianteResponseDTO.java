package org.example.dto;

import lombok.Builder;
import org.example.model.EstudianteGenero;

@Builder
public record EstudianteResponseDTO(
        Long lu,
        Integer dni,
        String nombres,
        String apellido,
        Integer edad,
        EstudianteGenero genero,
        String ciudadResidencia
) {
}
