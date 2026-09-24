package org.example.mapper;

import org.example.dto.EstudianteRequestDTO;
import org.example.dto.EstudianteResponseDTO;
import org.example.model.Estudiante;

public class EstudianteMapper {
    public static EstudianteResponseDTO toDto(Estudiante estudiante){
        return EstudianteResponseDTO.builder()
                .lu(estudiante.getLu())
                .dni(estudiante.getDni())
                .nombres(estudiante.getNombres())
                .apellido(estudiante.getApellido())
                .fechaNacimiento(estudiante.getFechaNacimiento())
                .genero(estudiante.getGenero())
                .ciudadResidencia(estudiante.getCiudadResidencia())
                .build();
    }

    public static Estudiante toEntity(EstudianteRequestDTO estudianteDto){
        Estudiante e = new Estudiante();
        e.setDni(estudianteDto.dni());
        e.setNombres(estudianteDto.nombres());
        e.setApellido(estudianteDto.apellido());
        e.setFechaNacimiento(estudianteDto.fechaNacimiento());
        e.setGenero(estudianteDto.genero());
        e.setCiudadResidencia(estudianteDto.ciudadResidencia());
        return e;
    }
}
