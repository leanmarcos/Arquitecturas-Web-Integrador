package org.example.dto;

import lombok.Builder;

/**
 * @param nombre se guarda sin espacios al principio ni al final
 */
@Builder
public record CarreraRequestDTO(String nombre, Integer duracion) {

    private static final int NOMBRE_MAX_LENGTH = 255;

    public CarreraRequestDTO {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la carrera es obligatorio.");
        }
        nombre = nombre.trim();
        if (nombre.length() > NOMBRE_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "El nombre de la carrera no puede superar los " + NOMBRE_MAX_LENGTH + " caracteres.");
        }
        if (duracion == null || duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser un número positivo.");
        }
    }
}
