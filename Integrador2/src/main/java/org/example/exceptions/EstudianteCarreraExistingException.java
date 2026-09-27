package org.example.exceptions;

public class EstudianteCarreraExistingException extends CustomException {
    public EstudianteCarreraExistingException() {
        super(
                "El estudiante ya está inscripto en esta carrera.",
                400,
                "Ya existe una inscripción del estudiante en la carrera"
        );
    }
}
