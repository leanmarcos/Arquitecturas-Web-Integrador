package org.example.mapper;

import org.example.dto.EstudianteCarreraRequestDTO;
import org.example.dto.EstudianteCarreraResponseDTO;
import org.example.model.Carrera;
import org.example.model.Estudiante;
import org.example.model.EstudianteCarrera;
import org.springframework.stereotype.Component;

@Component
public class EstudianteCarreraMapper {

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
                .antiguedad(request.antiguedad())
                .build();
    }
}
