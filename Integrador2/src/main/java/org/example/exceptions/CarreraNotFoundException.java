package org.example.exceptions;

public class CarreraNotFoundException extends CustomException{

    public CarreraNotFoundException(Long id) {
        super(
                "No se encontró la carrera con id " + id,
                404,
                "El número de id no corresponde a ninguna carrera registrada"
        );
    }


}
