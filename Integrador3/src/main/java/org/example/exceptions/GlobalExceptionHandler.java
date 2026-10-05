package org.example.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Atrapa las excepciones que salen de cualquier controller y las convierte en un {@link ErrorDto}
 * con el status HTTP que corresponde. Así los controllers no necesitan ningún try/catch.
 * <p>
 * Spring elige el handler más específico: si llega una {@link CustomException}, usa ese y no el de {@link Exception}.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Red de seguridad para cualquier cosa que no atrape otro handler: errores inesperados,
     * o una falla de infraestructura (base de datos caída, etc.).
     *
     * @return 500 con "Error interno del servidor"
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleDefaultException(Exception ex){
        log.error(ex.getMessage(), ex);
        ExceptionCode code = ExceptionCode.INTERNAL_ERROR;
        return ResponseEntity.status(code.getStatus()).body(code.toErrorDto("Error interno del servidor"));
    }

    /**
     * Excepciones de negocio (estudiante no encontrado, carrera duplicada, etc.).
     * Se loguea como warn y sin stack trace porque es una respuesta esperada, no una falla.
     *
     * @return el status del {@link ExceptionCode} que trae la excepción (404, 409, etc.) con su mensaje
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDto> handleCustomException(CustomException ex){
        log.warn(ex.getMessage());
        ExceptionCode code = ex.getExceptionCode();
        return ResponseEntity.status(code.getStatus()).body(code.toErrorDto(ex.getMessage()));
    }

    /**
     * Falla alguna anotación de validación (@NotBlank, @Positive, etc.) en un body que el controller recibe con @Valid.
     *
     * @return 400 con cada campo inválido y su mensaje en el detail
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidation(MethodArgumentNotValidException ex){
        Map<String, Object> detail = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach( err -> detail.put(err.getField(), err.getDefaultMessage()));

        ExceptionCode code = ExceptionCode.VALIDATION_ERROR;

        return ResponseEntity.status(code.getStatus()).body(code.toErrorDto("Campos inválidos", detail));
    }

    /**
     * Un path variable o query param no se puede convertir al tipo esperado,
     * por ejemplo /estudiantes/abc cuando se espera un número, o ?genero=XYZ con un valor que no está en el enum.
     *
     * @return 400 con el nombre del parámetro y el valor que mandaron
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex){
        ExceptionCode code = ExceptionCode.INVALID_PARAMETER;
        String message = String.format("Valor inválido '%s' para el parámetro '%s'", ex.getValue(), ex.getName());
        return ResponseEntity.status(code.getStatus()).body(code.toErrorDto(message));
    }

    /**
     * Falta un @RequestParam obligatorio en la URL.
     *
     * @return 400 con el nombre del parámetro que falta
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDto> handleMissingParameter(MissingServletRequestParameterException ex){
        ExceptionCode code = ExceptionCode.INVALID_PARAMETER;
        String message = String.format("Falta el parámetro obligatorio '%s'", ex.getParameterName());
        return ResponseEntity.status(code.getStatus()).body(code.toErrorDto(message));
    }

    /**
     * Jackson no pudo convertir el JSON del body en el RequestDTO. Hay tres casos:
     * <ul>
     *     <li>El constructor del DTO rechazó un dato: se devuelve su mensaje tal cual.</li>
     *     <li>Un campo tiene un valor del tipo equivocado (ej: texto en un número o un género que no existe):
     *     se indica qué campo falló y, si es un enum, cuáles son los valores permitidos.</li>
     *     <li>Cualquier otra cosa (JSON mal formado, body vacío): mensaje genérico, y queda en el log
     *     por si en realidad es un bug.</li>
     * </ul>
     *
     * @return 400 con el mensaje que corresponde a cada caso
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleMessageNotReadable(HttpMessageNotReadableException ex){
        ExceptionCode code = ExceptionCode.VALIDATION_ERROR;

        // Jackson envuelve lo que tire el constructor del DTO; solo IllegalArgumentException es una validación nuestra
        if(ex.getCause() instanceof ValueInstantiationException
                && ex.getMostSpecificCause() instanceof IllegalArgumentException iae){
            return ResponseEntity.status(code.getStatus()).body(code.toErrorDto(iae.getMessage()));
        }

        if(ex.getCause() instanceof InvalidFormatException ife){
            String field = extractFieldName(ife);
            String message = String.format("Valor inválido '%s' para el campo '%s'", ife.getValue(), field);

            Map<String, Object> detail = new HashMap<>();
            detail.put("campo", field);
            detail.put("valorRechazado", ife.getValue());
            if (ife.getTargetType().isEnum()) {
                detail.put("valoresPermitidos", ife.getTargetType().getEnumConstants());
            }

            return ResponseEntity.status(code.getStatus()).body(code.toErrorDto(message, detail));
        }

        log.warn(ex.getMessage(), ex);
        return ResponseEntity.status(code.getStatus()).body(code.toErrorDto("El cuerpo del request es inválido"));
    }

    /**
     * Se pidió una URL que no corresponde a ningún endpoint. Sin este handler terminaría como 500.
     *
     * @return 404 con la URL que se pidió
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDto> handleNoResource(NoResourceFoundException ex){
        ExceptionCode code = ExceptionCode.RESOURCE_NOT_FOUND;
        String message = String.format("No existe el recurso '/%s'", ex.getResourcePath());
        return ResponseEntity.status(code.getStatus()).body(code.toErrorDto(message));
    }

    /**
     * La URL existe pero no acepta ese método HTTP (ej: un POST donde solo hay GET).
     *
     * @return 405 con el método que se usó
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDto> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex){
        ExceptionCode code = ExceptionCode.METHOD_NOT_ALLOWED;
        String message = String.format("El método %s no está permitido en este recurso", ex.getMethod());
        return ResponseEntity.status(code.getStatus()).body(code.toErrorDto(message));
    }

    /**
     * Saca el nombre del campo que falló.
     *
     * @param ife el error de Jackson, que trae la ruta del campo dentro del JSON
     * @return el último elemento de la ruta, o "desconocido" si viene vacía
     */
    private String extractFieldName(InvalidFormatException ife) {
        List<JsonMappingException.Reference> path = ife.getPath();
        if (path.isEmpty()) {
            return "desconocido";
        }
        return path.get(path.size() - 1).getFieldName();
    }

}
