package org.example.exceptions;

import java.util.List;

public class ValidationException extends CustomException {

    public ValidationException(List<String> mensajes) {
        super(String.join(", ", mensajes), ExceptionCode.VALIDATION_ERROR);
    }
}
