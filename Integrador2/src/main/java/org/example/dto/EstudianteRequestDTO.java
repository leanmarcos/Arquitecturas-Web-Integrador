package org.example.dto;

import java.time.LocalDate;

public record EstudianteRequestDTO(
        Integer dni,
        String nombres,
        String apellido,
        LocalDate fechaNacimiento,
        String genero,
        String ciudadResidencia
) {
    public EstudianteRequestDTO {
        if (dni == null || dni <= 0) {
            throw new IllegalArgumentException("El DNI debe ser un número positivo.");
        }
        if (nombres == null || nombres.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        if (genero == null || genero.isBlank()) {
            throw new IllegalArgumentException("El género no puede estar vacío.");
        }
        if (ciudadResidencia == null || ciudadResidencia.isBlank()) {
            throw new IllegalArgumentException("La ciudad de residencia no puede estar vacía.");
        }
    }
}
