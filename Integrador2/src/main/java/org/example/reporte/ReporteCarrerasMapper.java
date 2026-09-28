package org.example.reporte;

import org.example.dto.EstudianteResponseDTO;
import org.example.mapper.EstudianteMapper;

import java.util.stream.Stream;

public class ReporteCarrerasMapper {

    /**
     * Separa una inscripción en sus eventos: siempre la inscripción y, si se graduó, también el egreso.
     */
    public static Stream<EventoCarrera> toEventos(ReporteFilaDTO fila) {
        EstudianteResponseDTO estudiante = EstudianteMapper.toDto(fila.estudiante());
        EventoCarrera inscripcion = new EventoCarrera(
                fila.nombreCarrera(), fila.anioInscripcion(), TipoEvento.INSCRIPCION, estudiante);

        if (fila.anioGraduacion() == null) {
            return Stream.of(inscripcion);
        }
        EventoCarrera egreso = new EventoCarrera(
                fila.nombreCarrera(), fila.anioGraduacion(), TipoEvento.EGRESO, estudiante);
        return Stream.of(inscripcion, egreso);
    }
}
