package org.example.mapper;

import org.apache.commons.csv.CSVRecord;
import org.example.dto.CarreraRequestDTO;
import org.example.dto.CarreraResponseDTO;
import org.example.model.Carrera;
import org.springframework.stereotype.Component;

@Component
public class CarreraMapper {

    public CarreraResponseDTO toDto(Carrera carrera){
        return CarreraResponseDTO.builder()
                .nombre(carrera.getNombre())
                .duracion(carrera.getDuracion())
                .build();
    }

    public Carrera toEntity(CarreraRequestDTO dto){
        return Carrera.builder()
                .nombre(dto.nombre().trim())
                .duracion(dto.duracion())
                .build();
    }

    public CarreraRequestDTO fromCsv(CSVRecord row) {
        return CarreraRequestDTO.builder()
                .nombre(row.get("carrera").trim())
                .duracion(Integer.parseInt(row.get("duracion").trim()))
                .build();
    }
}
