package org.example.dto;

import lombok.Builder;
import org.example.model.EstudianteGenero;

@Builder
public record EstudianteRequestDTO(
        Long lu,
        Integer dni,
        String nombres,
        String apellido,
        Integer edad,
        String genero,
        String ciudadResidencia
) {
    public EstudianteRequestDTO {
        if (lu == null || lu <= 0) {
            throw new IllegalArgumentException("La LU debe ser un número positivo.");
        }
        if (dni == null || dni <= 0) {
            throw new IllegalArgumentException("El DNI debe ser un número positivo.");
        }
        if (nombres == null || nombres.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }
        if (edad == null || edad <= 0) {
            throw new IllegalArgumentException("La edad debe ser un número positivo.");
        }
        if (EstudianteGenero.from(genero).isEmpty()) {
            throw new IllegalArgumentException("Género inválido: " + genero);
        }
        if (ciudadResidencia == null || ciudadResidencia.isBlank()) {
            throw new IllegalArgumentException("La ciudad de residencia no puede estar vacía.");
        }
    }
}
