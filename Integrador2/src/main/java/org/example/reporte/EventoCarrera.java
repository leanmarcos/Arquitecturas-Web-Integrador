package org.example.reporte;

import org.example.dto.EstudianteResponseDTO;

import java.time.Year;

/**
 * Un hecho del reporte de carreras (inscripción o egreso) con un único año, para poder agrupar por él.
 */
public record EventoCarrera(
        String carrera,
        Year anio,
        TipoEvento tipo,
        EstudianteResponseDTO estudiante
) {}
