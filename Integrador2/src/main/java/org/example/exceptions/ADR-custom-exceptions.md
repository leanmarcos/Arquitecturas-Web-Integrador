# ¿Por qué excepciones propias en vez de `RuntimeException`?

Un `throw new RuntimeException()` no dice qué pasó: quien lo atrapa (o el `DataLoader` al rechazar una fila) solo sabe
que algo falló. Por eso los errores de negocio lanzan una subclase de `CustomException`, que además del mensaje lleva
un código y una descripción:

```java
public abstract class CustomException extends RuntimeException {
    private final Integer errorCode;          // status HTTP que le corresponde (400, 404, 500)
    private final String errorDescription;    // detalle para el usuario
}
```

| Excepción | Código | Cuándo |
|---|---|---|
| `CarreraNotFoundException` | 404 | No existe la carrera (por id o por nombre) |
| `CarreraExistingException` | 400 | Ya hay una carrera con ese nombre |
| `CarreraConInscriptosException` | 400 | Se quiere borrar una carrera con inscriptos |
| `EstudianteNotFoundException` | 404 | No existe el estudiante (por LU o por DNI) |
| `EstudianteExistingException` | 400 | LU o DNI repetido |
| `EstudianteCarreraExistingException` | 400 | El estudiante ya está inscripto en esa carrera |
| `UnexpectedException` | 500 | Algo que no debería pasar (ej: `save` no devolvió la entidad) |

Los datos mal formados no usan `CustomException`: los rechaza el constructor del `RequestDTO` con
`IllegalArgumentException` (ver `dto/ADR-dto.md`), igual que los services con parámetros vacíos o inválidos (ej: id
nulo). Es la excepción estándar de Java para un argumento inválido y una propia no agregaría nada.

## ¿Cómo devolvemos los errores?

No hay DTO de error: el service lanza la excepción y la atrapa quien lo llama. El `DataLoader` atrapa el error de
cada fila, guarda el mensaje como motivo del rechazo y sigue con la siguiente.
