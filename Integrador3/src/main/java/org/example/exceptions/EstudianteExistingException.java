package org.example.exceptions;

public class EstudianteExistingException extends CustomException {

    public EstudianteExistingException(Long lu) {
        super(String.format("Ya existe un estudiante con LU %d", lu), ExceptionCode.STUDENT_NUMBER_DUPLICATED);
    }

    public EstudianteExistingException(Integer dni) {
        super(String.format("Ya existe un estudiante con DNI %d", dni), ExceptionCode.STUDENT_DOCUMENT_DUPLICATED);
    }
}
