# ¿Por qué `Conteo` y `Rechazo` son records anidados dentro de `DataResult`?

`DataResult` guarda, por cada CSV, cuántas filas se cargaron y cuáles se rechazaron y por qué. Eso se modela con
dos records declarados **dentro** de `DataResult`:

```java
public record DataResult(Conteo carreras, Conteo estudiantes, Conteo inscripciones) {
    public record Conteo(int cargados, List<Rechazo> rechazados) {}
    public record Rechazo(long fila, String motivo) {}
}
```

- **Solo tienen sentido ahí:** no se usan fuera del resultado del loader. Anidarlos deja claro que son parte de
  `DataResult` y evita archivos sueltos en el paquete.
- **El nombre queda en contexto:** desde afuera se lee `DataResult.Conteo` / `DataResult.Rechazo`, así que nombres
  cortos no se confunden con otros del proyecto.
- **No dependen de la instancia:** un record anidado es implícitamente `static`, no guarda referencia al
  `DataResult` que lo contiene y se crea sin tenerlo: `new DataResult.Rechazo(3, "...")`.

**`List<Rechazo>` en vez de `Map<Long, String>`:** mantiene el orden del CSV y `rechazo.fila()` /
`rechazo.motivo()` se leen mejor que `getKey()` / `getValue()`. Agregar un dato (ej: tipo de error) es sumar un
campo, sin cambiar firmas.

**Cuándo sacarlos a su propio archivo:** si otra clase empieza a usarlos por fuera de `DataResult`.
