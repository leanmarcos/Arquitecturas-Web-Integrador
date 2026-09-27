package org.example.mapper;

import org.apache.commons.csv.CSVRecord;
import org.example.dto.EstudianteRequestDTO;
import org.example.dto.EstudianteResponseDTO;
import org.example.model.Estudiante;
import org.example.model.EstudianteGenero;

public class EstudianteMapper {
    public static EstudianteResponseDTO toDto(Estudiante estudiante){
        return EstudianteResponseDTO.builder()
                .lu(estudiante.getLu())
                .dni(estudiante.getDni())
                .nombres(estudiante.getNombres())
                .apellido(estudiante.getApellido())
                .edad(estudiante.getEdad())
                .genero(estudiante.getGenero())
                .ciudadResidencia(estudiante.getCiudadResidencia())
                .build();
    }

    public static Estudiante toEntity(EstudianteRequestDTO estudianteDto){
        Estudiante e = new Estudiante();
        e.setLu(estudianteDto.lu());
        e.setDni(estudianteDto.dni());
        e.setNombres(estudianteDto.nombres());
        e.setApellido(estudianteDto.apellido());
        e.setEdad(estudianteDto.edad());
        // el DTO ya validó que el género exista
        e.setGenero(EstudianteGenero.from(estudianteDto.genero()).orElseThrow());
        e.setCiudadResidencia(estudianteDto.ciudadResidencia());
        return e;
    }

    public static EstudianteRequestDTO fromCsv(CSVRecord row){
        return EstudianteRequestDTO.builder()
                .lu(Long.parseLong(row.get("LU").trim()))
                .dni(Integer.parseInt(row.get("DNI").trim()))
                .nombres(row.get("nombre").trim())
                .apellido(row.get("apellido").trim())
                .edad(Integer.parseInt(row.get("edad").trim()))
                .genero(row.get("genero").trim())
                .ciudadResidencia(row.get("ciudad").trim())
                .build();
    }
}
