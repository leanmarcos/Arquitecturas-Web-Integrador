package org.example.mapper;

import org.example.dto.CarreraInscriptosResponseDTO;

public class CarreraMapper {
    public static CarreraInscriptosResponseDTO toDto(Object[] fila){
        return CarreraInscriptosResponseDTO.builder()
                .nombre((String) fila[0])
                .totalInscriptos((Long) fila[1])
                .build();
    }
}
