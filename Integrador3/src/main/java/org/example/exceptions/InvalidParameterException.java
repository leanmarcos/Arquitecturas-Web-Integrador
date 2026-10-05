package org.example.exceptions;

public class InvalidParameterException extends CustomException {

    public InvalidParameterException(String parametro, Object valor) {
        super(
                String.format("Valor inválido '%s' para el parámetro '%s'", valor, parametro),
                ExceptionCode.INVALID_PARAMETER
        );
    }
}
