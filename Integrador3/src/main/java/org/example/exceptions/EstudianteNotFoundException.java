package org.example.exceptions;

public class EstudianteNotFoundException extends CustomException {

    public EstudianteNotFoundException(Long lu) {
        super(
                "No se encontró el estudiante con LU " + lu,
                404,
                "La LU no corresponde a ningún estudiante registrado"
        );
    }

    public EstudianteNotFoundException(Integer dni) {
        super(
                "No se encontró el estudiante con DNI " + dni,
                404,
                "El DNI no corresponde a ningún estudiante registrado"
        );
    }
}
