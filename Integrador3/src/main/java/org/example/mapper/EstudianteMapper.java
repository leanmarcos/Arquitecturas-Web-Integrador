package org.example.mapper;

import org.apache.commons.csv.CSVRecord;
import org.example.dto.EstudianteRequestDTO;
import org.example.dto.EstudianteResponseDTO;
import org.example.model.Estudiante;
import org.example.model.EstudianteGenero;
import org.springframework.stereotype.Component;

@Component
public class EstudianteMapper {

    public EstudianteResponseDTO toDto(Estudiante estudiante){
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

    public Estudiante toEntity(EstudianteRequestDTO estudianteDto){
        return Estudiante.builder()
                .lu(estudianteDto.lu())
                .dni(estudianteDto.dni())
                .nombres(estudianteDto.nombres().trim())
                .apellido(estudianteDto.apellido().trim())
                .edad(estudianteDto.edad())
                .genero(EstudianteGenero.from(estudianteDto.genero()).orElseThrow())
                .ciudadResidencia(estudianteDto.ciudadResidencia().trim())
                .build();
    }

    public EstudianteRequestDTO fromCsv(CSVRecord row){
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
