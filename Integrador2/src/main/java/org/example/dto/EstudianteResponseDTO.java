package org.example.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EstudianteResponseDTO(
        Long lu,
        Integer dni,
        String nombres,
        String apellido,
        LocalDate fechaNacimiento,
        String genero,
        String ciudadResidencia
) {
}
