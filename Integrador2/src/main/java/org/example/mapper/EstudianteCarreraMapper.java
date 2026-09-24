package org.example.mapper;

import org.example.dto.EstudianteCarreraResponseDTO;
import org.example.model.EstudianteCarrera;

public class EstudianteCarreraMapper {
    public static EstudianteCarreraResponseDTO toDto(EstudianteCarrera inscripcion){
        return EstudianteCarreraResponseDTO.builder()
                .id(inscripcion.getId())
                .luEstudiante(inscripcion.getEstudiante().getLu())
                .idCarrera(inscripcion.getCarrera().getId())
                .nombreCarrera(inscripcion.getCarrera().getNombre())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .fechaGraduacion(inscripcion.getFechaGraduacion())
                .graduado(inscripcion.isGraduado())
                .build();
    }
}
