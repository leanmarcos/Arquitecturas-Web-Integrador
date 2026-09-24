# Custom Exceptions

## Por qué
Las excepciones aparecen cuando hay un error pero nunca especificamos cuál

Lo que la mayoría del tiempo hacemos es:

```
throw new RuntimeException RuntimeException
```

Eso no dá información sobre lo que pasó, el usuario solo sabe que algo falló pero no qué y puede volver a hacer el mismo error

Para dar más información, se podría considerar tener:
- HttpStatus: El código de error (404, 400, 500, etc.)
- errorDetails: Detalles extra del error si es necesario
- message: El mensaje del error

La idea sería generar una clase base que descienda de Runtime Exception

Es el mismo Runtime Exception el que te dá el mensaje de error

```
public abstract class CustomException extends RuntimeException {

    private final HttpStatus HttpStatus;
    private final String errorDescription;

    protected CustomException(String errorCode, String message, String errorDescription) {
        super(message);   // el mensaje va al constructor de RuntimeException, no lo dupliques como campo
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    
    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
    
    public abstract HttpStatus getHttpStatus();
}
```

Esa es es la base, los hijos derivan de ahí y tienen los mismos atributos

Entonces supongamos que el usuario pidió ver el estudiante `10` pero no existe, para eso creamos

```
public class EstudianteNotFoundException extends CustomException {
    public EstudianteNotFoundException(Long lu) {
        super("ESTUDIANTE_NOT_FOUND", 
              "No se encontró el estudiante con LU " + lu, 
              "El número de libreta no corresponde a ningún estudiante registrado");
    }
    
    @Override
    public HttpStatus getHttpStatus(){
           return HttpStatus.NOT_FOUND;
    }
}
```

En resumen la jerarquia es asi:

````mermaid
flowchart TD
    A["RuntimeException"]
    B["CustomException<br/><small>abstract · errorCode, httpStatus</small>"]

    A --> B

    subgraph E["Excepciones concretas (ejemplos)"]
        C["<b>EstudianteNotFoundException</b><br/>EST-001 · 404 Not Found"]
        D["<b>DniDuplicadoException</b><br/>EST-002 · 409 Conflict"]
        F["<b>CarreraNotFoundException</b><br/>CAR-001 · 404 Not Found"]
    end

    B --> C
    B --> D
    B --> F

    class A runtime
    class B custom
    class C,D,F exception

````

## Excepciones Custom vs Jakarta.persistence

Jakarta.persistence ya tiene excepciones para varios casos que hemos usado en el TPE

- EntityNotFoundException()
- IllegalArgumentException()

Pero esto no cubre casos en las que se violan reglas de negocio específicas como los CHECK en la base de datos

Por ejemplo: Alguien quiere cambiar la fecha de nacimiento de un alumno para hacerlo menor de edad

No hay excepción estándar para eso

## Convenciones
Se puede trabajar de forma organizada con convenciones en los errorCode

La forma más facil es poner algo corto pero muy descriptivo como `ESTUDIANTE_NOT_FOUND` , `ESTUDIANTE_CARRERA_CHECK_VIOLATION`, etc.

La otra forma es definir abreviaciones para cada entidad y código, por ejemplo:

- CAR para Carrera
- EST para Estudiante
- ESC para EstudianteCarrera

Y después tener un conjunto de códigos

Por ejemplo, el 001 sería NotFound

Entonces cuando alguien pide el Estudiante número `10` y no existe, se usa EST-001

Eso queda a criterio del equipo

Otros ejemplos:
```
"ESTUDIANTE_NOT_FOUND"        // más descriptivo, sin números
"E001"                         // más corto
"404-EST-NOTFOUND"             // incluye el status HTTP en el código
"student.not_found"            // estilo i18n/properties key
```

## Donde usarlos
En el Service

## ¿Cortan la aplicación?
Si pero si se los controla de buena forma no

Lo ideal es desde el Controller "atraparlos" y crear un ErrorResponseDTO con eso (Hacer en TP3)

```
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomException(CustomException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
            ex.getErrorCode(),
            ex.getMessage(),
            ex.getErrorDescription()
        );
        HttpStatus status = mapErrorCodeToStatus(ex.getErrorCode());
        return ResponseEntity.status(status).body(error);
    }
}
```