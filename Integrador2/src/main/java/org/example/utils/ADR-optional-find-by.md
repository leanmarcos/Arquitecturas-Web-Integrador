# ¿Por qué los `findBy...` devuelven `Optional`?

`em.find(...)` devuelve `null` si no existe. El repository lo envuelve con `Optional.ofNullable(...)` para que la
firma avise que el resultado puede no estar. El repository solo informa si lo encontró; **qué hacer si no está lo
decide el service.**

Pero el gran beneficio no es solo avisar que puede faltar: `Optional` permite **encadenar operaciones** sin escribir
`if (isEmpty())` ni chequeos de `null`.

## Cómo se usa en el service

```java
return estudianteRepository.findByLu(em, lu)
        .map(EstudianteMapper::toDto)
        .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado con LU: " + lu));
```

Lo que **evitamos** escribir (hace lo mismo, pero con chequeo y `get()` a mano):

```java
Optional<Estudiante> resultado = estudianteRepository.findByLu(em, lu);
if (resultado.isEmpty()) {
    throw new EntityNotFoundException("Estudiante no encontrado con LU: " + lu);
}
return EstudianteMapper.toDto(resultado.get());
```

Usar `Optional` con `isEmpty()` + `get()` es lo mismo que chequear `null` con otra sintaxis: se pierde la ventaja.

- **`.map(...)`**: si hay valor, le aplica la función y lo devuelve envuelto (`Optional<Estudiante>` →
  `Optional<EstudianteResponseDTO>`). Si está vacío, no llama a nada y sigue vacío. Por eso se puede encadenar sin
  preguntar antes si hay valor.
- **`EstudianteMapper::toDto`** (method reference): es una forma corta de `estudiante -> EstudianteMapper.toDto(estudiante)`.
  `.map` espera una función que reciba un `Estudiante` y devuelva algo. Como `toDto` recibe un `Estudiante` y
  devuelve un `EstudianteResponseDTO`, encaja con esa forma y Java la usa directamente como esa función.
  Es solo legibilidad: no cambia el rendimiento respecto de la lambda.
- **`.orElseThrow(() -> ...)`**: si hay valor lo devuelve; si está vacío, tira la excepción. Recibe una lambda
  (un `Supplier`) por **rendimiento**: esa lambda solo se ejecuta si el `Optional` está vacío, así que si el
  estudiante existe la excepción nunca se crea (crear una excepción es caro porque captura el stack trace).
  Por la misma razón se prefiere `orElseGet(() -> ...)` sobre `orElse(...)`: `orElse` evalúa su argumento siempre.
