package org.example.exceptions;

public class CarreraConInscriptosException extends CustomException {
    public CarreraConInscriptosException(Long id) {
        super(
                "No se puede eliminar la carrera con id " + id + " porque tiene estudiantes inscriptos.",
                400,
                "Una carrera solo se puede eliminar si no tiene inscripciones"
        );
    }
}
