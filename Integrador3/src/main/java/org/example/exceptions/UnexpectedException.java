package org.example.exceptions;

public class UnexpectedException extends CustomException {
    public UnexpectedException() {
        super(
                "Error innesperado",
                500,
                "Ocurrió un error innesperado"
        );
    }
}
