package org.example.reporte;

import org.example.model.Estudiante;

import java.time.Year;

/**
 * Fila plana del reporte de carreras: una inscripción de un estudiante a una carrera.
 *
 * @param anioGraduacion {@code null} si el estudiante no se graduó
 */
public record ReporteFilaDTO(
        String nombreCarrera,
        Year anioInscripcion,
        Year anioGraduacion,
        Estudiante estudiante
) {}
