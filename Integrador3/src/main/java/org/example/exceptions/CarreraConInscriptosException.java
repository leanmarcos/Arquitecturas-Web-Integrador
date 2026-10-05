package org.example.exceptions;

public class CarreraConInscriptosException extends CustomException {

    public CarreraConInscriptosException(Long id) {
        super(
                String.format("No se puede eliminar la carrera con id %d porque tiene estudiantes inscriptos", id),
                ExceptionCode.MAJOR_HAS_ENROLLMENTS
        );
    }
}
