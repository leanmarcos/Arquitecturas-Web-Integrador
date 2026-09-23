package org.example.mapper;

import org.example.dto.EstudianteRequestDTO;
import org.example.dto.EstudianteResponseDTO;
import org.example.model.Estudiante;

public class EstudianteMapper {
    public static EstudianteResponseDTO toDto(Estudiante estudiante){
        return new EstudianteResponseDTO(
                estudiante.getLu(),
                estudiante.getDni(),
                estudiante.getNombres(),
                estudiante.getApellido(),
                estudiante.getFechaNacimiento(),
                estudiante.getGenero(),
                estudiante.getCiudadResidencia());
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
