package org.example.dto;

import lombok.Builder;

@Builder
public record CarreraInscriptosResponseDTO(
        String nombre,
        Long totalInscriptos
) {
}
