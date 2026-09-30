# DTOs de entrada y salida

## Validación manual en el constructor del record

Cada `RequestDTO` valida sus datos en el constructor compacto y lanza `IllegalArgumentException` si algo está mal:

```java
public record EstudianteCarreraRequestDTO(Integer dni, String nombreCarrera, Year anioInscripcion, Year anioGraduacion) {
    public EstudianteCarreraRequestDTO {
        if (anioInscripcion == null) {
            throw new IllegalArgumentException("El año de inscripción es obligatorio.");
        }
        // ...
    }
}
```

**Alternativa considerada:** Bean Validation (`@NotBlank`, `@Positive`, ... + `@Valid`). Se descartó porque:

- Sin Spring las anotaciones no hacen nada solas: hay que sumar hibernate-validator y llamar al `Validator` a mano
  en cada service.
- Con el constructor no puede existir un DTO inválido, venga de `Main` o del CSV.
- Reglas entre campos (graduación ≥ inscripción) quedan en un `if`, sin anotaciones custom.

## Por qué los services devuelven un `ResponseDTO` y no la entidad

- **No exponer la entidad:** el que llama recibe solo los datos que decidimos mostrar.
- **Relaciones lazy:** las entidades tienen `@OneToMany` lazy; si se devuelven y se accede a la relación con el
  `EntityManager` ya cerrado, falla. El DTO se arma adentro del service, con todo cargado.
- **Contrato estable:** cambiar la entidad no obliga a cambiar a quien usa el service.

La excepción son `findEntityByDni` y `findEntityByName`: devuelven la entidad porque los usa
`EstudianteCarreraService` dentro de su propia transacción, para armar la inscripción.

Por eso `Request` y `Response` son DTOs separados aunque a veces repitan campos: uno controla lo que entra y el otro
lo que sale.
