package org.example.dto;

import lombok.Builder;

/**
 * @param genero nombre en español (ej: "Masculino"), no la constante del enum
 */
@Builder
public record EstudianteResponseDTO(
        Long lu,
        Integer dni,
        String nombres,
        String apellido,
        Integer edad,
        String genero,
        String ciudadResidencia
) {
    @Override
    public String toString() {
        return String.format("LU %-7d | %-25s | DNI %-9d | %3d años | %-13s | %s",
                lu, apellido + ", " + nombres, dni, edad, genero, ciudadResidencia);
    }
}
