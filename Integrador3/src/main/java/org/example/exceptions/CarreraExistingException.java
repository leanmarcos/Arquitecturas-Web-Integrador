package org.example.exceptions;

public class CarreraExistingException extends CustomException {

    public CarreraExistingException(String nombre) {
        super(String.format("Ya existe una carrera con nombre %s", nombre), ExceptionCode.MAJOR_NAME_DUPLICATED);
    }
}
