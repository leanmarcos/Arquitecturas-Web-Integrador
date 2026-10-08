package org.example.exceptions;

public class GeneroNotFoundException extends CustomException {

    public GeneroNotFoundException(String genero) {
        super(String.format("No se encontró la carrera con nombre %s", genero), ExceptionCode.GENERO_NOT_FOUND);
    }
}
