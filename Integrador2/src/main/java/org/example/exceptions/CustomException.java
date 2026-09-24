package org.example.exceptions;

public abstract class CustomException extends RuntimeException {

    private final Integer errorCode;
    private final String errorDescription;

    protected CustomException(String message, Integer errorCode, String errorDescription) {
        super(message);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

}