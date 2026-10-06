package org.example.exceptions;

public class DocumentException extends CustomException {

    public DocumentException(Integer dni) {
        super(String.format("Ya existe un estudiante con DNI %d", dni), ExceptionCode.STUDENT_DOCUMENT_DUPLICATED);
    }
}
