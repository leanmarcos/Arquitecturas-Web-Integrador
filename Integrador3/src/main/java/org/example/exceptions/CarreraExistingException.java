package org.example.exceptions;

public class CarreraExistingException extends CustomException {
    public CarreraExistingException(){
        super(
                "Carrera igual existente",
                400,
                "Ya existe una carrera con esas características"
        );
    }
}
