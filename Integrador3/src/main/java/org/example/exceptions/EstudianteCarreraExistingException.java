package org.example.exceptions;

public class EstudianteCarreraExistingException extends CustomException {

    public EstudianteCarreraExistingException(Integer dni, String nombreCarrera) {
        super(
                String.format("El estudiante con DNI: %d ya está inscripto en la carrera %s", dni, nombreCarrera),
                ExceptionCode.ENROLLMENT_DUPLICATED
        );
    }
}
