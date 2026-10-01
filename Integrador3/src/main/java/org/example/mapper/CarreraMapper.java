package org.example.mapper;

import org.apache.commons.csv.CSVRecord;
import org.example.dto.CarreraRequestDTO;
import org.example.model.Carrera;

public class CarreraMapper {
    public static Carrera toEntity(CarreraRequestDTO dto){
        return Carrera.builder()
                .nombre(dto.nombre())
                .duracion(dto.duracion())
                .build();
    }

    public static CarreraRequestDTO fromCsv(CSVRecord row) {
        return CarreraRequestDTO.builder()
                .nombre(row.get("carrera").trim())
                .duracion(Integer.parseInt(row.get("duracion").trim()))
                .build();
    }
}
