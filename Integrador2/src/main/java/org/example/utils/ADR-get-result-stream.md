# ¿Por qué los `findBy...` con query usan `getResultStream().findFirst()`?

`getSingleResult()` no devuelve `null` si la query no encuentra nada: tira `NoResultException`. Por eso
`Optional.ofNullable(query.getSingleResult())` nunca queda vacío y falla justo en el caso que queríamos cubrir.

`getResultStream().findFirst()` devuelve un `Optional` vacío si no hay resultados, sin excepción, y sigue el mismo
criterio que [ADR-optional-find-by](ADR-optional-find-by.md): el repository informa y el service decide.

```java
return em.createQuery("SELECT e FROM Estudiante e WHERE e.dni = :dni", Estudiante.class)
        .setParameter("dni", dni)
        .getResultStream()
        .findFirst();
```

Lo que **evitamos**: atrapar `NoResultException` para devolver `Optional.empty()`. Usa una excepción para un caso
normal (que no exista) y ensucia el repository con un `try/catch`.

Solo aplica a campos únicos (`dni`, `nombre` de carrera): si la query pudiera devolver varias filas, `findFirst()`
tomaría una sin avisar.
