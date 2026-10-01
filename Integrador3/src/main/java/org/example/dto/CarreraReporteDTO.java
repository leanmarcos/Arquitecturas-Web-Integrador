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

    /**
     * Clave compuesta (carrera y año) utilizada para agrupar y ordenar naturalmente el reporte:
     * alfabéticamente por nombre de carrera (A-Z) y cronológicamente ascendente por año.
     *
     * @param carrera Nombre de la carrera.
     * @param anio Año evaluado.
     */
    public record ClaveReporte(String carrera, Year anio) implements Comparable<ClaveReporte> {
        @Override
        public int compareTo(ClaveReporte o) {
            int cmp = this.carrera.compareToIgnoreCase(o.carrera);
            if (cmp != 0) return cmp;
            return this.anio.compareTo(o.anio);
        }
    }
}
