package org.example.exceptions;

public class EstudianteExistingException extends CustomException {
    public EstudianteExistingException() {
        super(
                "Ya existe un estudiante con esa LU o DNI.",
                400,
                "La LU y el DNI no pueden repetirse entre estudiantes"
        );
    }
}
