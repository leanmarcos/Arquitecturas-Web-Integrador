# ¿Por qué `SELECT new` en vez de `Object[]` para consultas agregadas?

Cuando un `SELECT` de JPQL trae columnas sueltas (`c.nombre, COUNT(ce)`), no hay una entidad donde guardarlas y JPA
devuelve cada fila como `Object[]`:

- `[0]` (`String`): nombre de la carrera.
- `[1]` (`Long`): cantidad de inscriptos.

Esa alternativa obliga a un mapper que castee por posición:

```java
.nombre((String) fila[0])
.totalInscriptos((Long) fila[1])
```

Lo que **evitamos**:

- **Casts frágiles:** si cambia el orden o el tipo de una columna del `SELECT`, compila igual y falla en ejecución
  con `ClassCastException`.
- **No escala:** con 2 columnas es manejable, pero con 5 o 6 hay que recordar qué índice es cada una y castear
  cosa por cosa.

Por eso usamos una **constructor expression**, que arma el DTO directamente en la consulta:

```sql
SELECT new org.example.dto.CarreraInscriptosResponseDTO(c.nombre, COUNT(ce))
FROM Carrera c
JOIN c.estudiantes ce
GROUP BY c.nombre
ORDER BY COUNT(ce) DESC
```

Hibernate valida el constructor al parsear la consulta, así que un error de orden o de tipo aparece antes y no
queda escondido en un cast. Esta consulta ya no pasa por `CarreraMapper`.

Solo aplica a proyecciones y agregados. Si la consulta devuelve una entidad, se devuelve la entidad y el service la
mapea al DTO: así el repository no depende de los DTOs y la entidad queda gestionada por el `EntityManager`.
