package org.example.dto;

import java.time.LocalDate;

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
