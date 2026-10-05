package org.example.exceptions;

import lombok.Getter;
import org.example.dto.ErrorDto;
import org.springframework.http.HttpStatus;

import java.util.Locale;
import java.util.Map;

@Getter
public enum ExceptionCode {
    // Estudiante
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND),
    STUDENT_DOCUMENT_DUPLICATED(HttpStatus.CONFLICT),
    STUDENT_NUMBER_DUPLICATED(HttpStatus.CONFLICT),

    // Carrera
    MAJOR_NOT_FOUND(HttpStatus.NOT_FOUND),
    MAJOR_NAME_DUPLICATED(HttpStatus.CONFLICT),
    MAJOR_HAS_ENROLLMENTS(HttpStatus.CONFLICT),

    // Inscripción
    ENROLLMENT_DUPLICATED(HttpStatus.CONFLICT),

    // Generales
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);


    private final HttpStatus status;

    ExceptionCode(HttpStatus status){
        this.status = status;
    }

    public ErrorDto toErrorDto(){
        return ErrorDto.builder()
                .error(this.getCode())
                .status(this.getStatus().value())
                .build();
    }

    public ErrorDto toErrorDto(String message){
        return ErrorDto.builder()
                .error(this.getCode())
                .message(message)
                .status(this.getStatus().value())
                .build();
    }

    public ErrorDto toErrorDto(String message, Map<String, Object> detail){
        return ErrorDto.builder()
                .error(this.getCode())
                .message(message)
                .status(this.getStatus().value())
                .detail(detail)
                .build();
    }

    public String getCode(){
        return this.name().toLowerCase(Locale.ROOT);
    }
}
