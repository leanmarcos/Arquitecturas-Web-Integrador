package org.example.exceptions;

public class CarreraNotFoundException extends CustomException {

    public CarreraNotFoundException(Long id) {
        super(String.format("No se encontró la carrera con id %d", id), ExceptionCode.MAJOR_NOT_FOUND);
    }

    public CarreraNotFoundException(String nombre) {
        super(String.format("No se encontró la carrera con nombre %s", nombre), ExceptionCode.MAJOR_NOT_FOUND);
    }
}
