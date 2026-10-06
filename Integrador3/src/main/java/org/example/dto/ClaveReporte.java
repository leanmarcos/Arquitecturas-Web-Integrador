package org.example.dto;

import java.time.Year;

/**
 * Clave compuesta (carrera y año) que comparten {@link FilaReporteDTO} y {@link CarreraReporteDTO}: sirve para
 * juntar las filas de inscriptos y egresados de la misma carrera y año en una sola fila del reporte.
 *
 * @param carrera Nombre de la carrera.
 * @param anio Año evaluado.
 */
public record ClaveReporte(String carrera, Year anio) {
}
