package org.example.dto;

import lombok.Builder;

import java.time.Year;

/**
 * DTO que representa una fila del reporte de carreras con estadísticas de inscriptos y egresados por año.
 *
 * @param nombreCarrera Nombre de la carrera.
 * @param anio Año calendario del registro.
 * @param inscriptos Cantidad de estudiantes inscriptos en dicho año.
 * @param egresados Cantidad de estudiantes egresados en dicho año.
 */
@Builder
public record CarreraReporteDTO(String nombreCarrera, Year anio, Long inscriptos, Long egresados) {
}
