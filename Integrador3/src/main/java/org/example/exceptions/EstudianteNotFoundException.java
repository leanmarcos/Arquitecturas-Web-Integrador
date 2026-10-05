package org.example.exceptions;

public class EstudianteNotFoundException extends CustomException {

    public EstudianteNotFoundException(Long lu) {
        super(String.format("No se encontró el estudiante con LU %d", lu), ExceptionCode.STUDENT_NOT_FOUND);
    }

    public EstudianteNotFoundException(Integer dni) {
        super(String.format("No se encontró el estudiante con DNI %d", dni), ExceptionCode.STUDENT_NOT_FOUND);
    }
}
