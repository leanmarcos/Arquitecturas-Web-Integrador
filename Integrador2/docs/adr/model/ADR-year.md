# ¿Por qué `anioInscripcion` y `anioGraduacion` son `Year`?

El CSV trae solo el año de inscripción y de graduación. Con `LocalDate` había que inventar el día y el mes (ej.
`01/01`), y guardaríamos un dato que no tenemos. `java.time.Year` representa exactamente eso: un año y nada más.

Además, al ser un tipo propio (y no un `Integer` suelto), la firma deja claro qué representa el valor y se puede
operar como año (`Year.now()`, `getValue()`, comparaciones).

## ¿Cómo lo persiste Hibernate?

Desde **Hibernate 6**, `Year` es un tipo básico soportado: lo traduce solo a una columna `INTEGER` y lo vuelve a
convertir en `Year` al leer. Por eso la entidad lo usa directamente, sin nada extra.

En versiones anteriores Hibernate no sabía mapear `Year`, así que había que guardarlo como `int` o escribir un
`AttributeConverter<Year, Integer>` a mano.

Como en la base queda un número, las consultas JPQL (`GROUP BY ec.anioInscripcion`) y el `@Check`
(`anio_graduacion >= anio_inscripcion`) comparan años sin conversiones.
