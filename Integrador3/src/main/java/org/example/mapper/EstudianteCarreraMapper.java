package org.example.mapper;

import org.apache.commons.csv.CSVRecord;
import org.example.dto.EstudianteCarreraRequestDTO;
import org.example.dto.EstudianteCarreraResponseDTO;
import org.example.dto.EstudianteResponseDTO;
import org.example.dto.EstudiantesPorCarreraResponseDTO;
import org.example.model.Carrera;
import org.example.model.Estudiante;
import org.example.model.EstudianteCarrera;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.List;
import java.util.Map;

@Component
public class EstudianteCarreraMapper {
    /** En el CSV, un año de graduación 0 significa que el estudiante no se graduó. */
    private static final int SIN_GRADUACION = 0;

    public EstudianteCarreraResponseDTO toDto(EstudianteCarrera inscripcion){
        return EstudianteCarreraResponseDTO.builder()
                .luEstudiante(inscripcion.getEstudiante().getLu())
                .idCarrera(inscripcion.getCarrera().getId())
                .nombreCarrera(inscripcion.getCarrera().getNombre())
                .anioInscripcion(inscripcion.getAnioInscripcion())
                .anioGraduacion(inscripcion.getAnioGraduacion())
                .graduado(inscripcion.isGraduado())
                .antiguedad(inscripcion.getAntiguedad())
                .build();
    }

    public EstudianteCarrera toEntity(EstudianteCarreraRequestDTO request, Estudiante estudiante, Carrera carrera){
        return EstudianteCarrera.builder()
                .estudiante(estudiante)
                .carrera(carrera)
                .anioInscripcion(request.anioInscripcion())
                .anioGraduacion(request.anioGraduacion())
                .build();
    }

    /**
     * Convierte una fila de {@code estudianteCarrera.csv} en un request. {@code id_estudiante} es el DNI del
     * estudiante; {@code id_carrera} es el id de {@code carreras.csv}, no el de la base, por eso se traduce a nombre con
     * {@code nombresCarreraPorIdCsv}. La {@code antiguedad} del CSV se ignora: se calcula a partir de los años.
     *
     * @param nombresCarreraPorIdCsv nombre de cada carrera según su id en {@code carreras.csv}
     */
    public EstudianteCarreraRequestDTO fromCsv(CSVRecord row, Map<Long, String> nombresCarreraPorIdCsv) {
        Long idCarreraCsv = Long.parseLong(row.get("id_carrera").trim());
        int graduacion = Integer.parseInt(row.get("graduacion").trim());

        return EstudianteCarreraRequestDTO.builder()
                .dni(Integer.parseInt(row.get("id_estudiante").trim()))
                .nombreCarrera(nombresCarreraPorIdCsv.get(idCarreraCsv))
                .anioInscripcion(Year.parse(row.get("inscripcion").trim()))
                .anioGraduacion(graduacion == SIN_GRADUACION ? null : Year.of(graduacion))
                .build();
    }

    public EstudiantesPorCarreraResponseDTO toDto(
            List<EstudianteResponseDTO> estudiantes,
            String nombreCarrera,
            String ciudadResidencia){
        return EstudiantesPorCarreraResponseDTO.builder()
                .carrera(nombreCarrera)
                .ciudad(ciudadResidencia)
                .estudiantes(estudiantes)
                .build();
    }
}
