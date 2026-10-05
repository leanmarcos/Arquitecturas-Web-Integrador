package org.example.exceptions;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    protected CustomException(String message, ExceptionCode exceptionCode){
        super(message);
        this.exceptionCode = exceptionCode;
    }

    protected CustomException(String message, ExceptionCode exceptionCode, Throwable cause){
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }

}
