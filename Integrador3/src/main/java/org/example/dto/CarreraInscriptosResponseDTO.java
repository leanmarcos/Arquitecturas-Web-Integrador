package org.example.dto;

import lombok.Builder;

@Builder
public record CarreraInscriptosResponseDTO(
        String nombre,
        Long totalInscriptos
) {
    @Override
    public String toString() {
        return String.format("%-25s | %d inscriptos", nombre, totalInscriptos);
    }
}
