package org.example.dto;

import java.time.Year;

/**
 * DTO intermedio utilizado para transportar el resultado de proyecciones JPQL agregadas
 * (conteo de inscriptos o egresados por carrera y año).
 *
 * @param nombreCarrera Nombre de la carrera.
 * @param anio Año de inscripción o graduación.
 * @param cantidad Cantidad total de estudiantes asociados.
 */
public record FilaReporteDTO(String nombreCarrera, Year anio, Long cantidad) {
}
