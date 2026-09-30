# TODO

## Estudiante

- [x] (Bajo) `findAllByGenero` distingue mayúsculas: "Femenino" ≠ "femenino". Resuelto pasando `genero` a enum (`EstudianteGenero.from`).

## EstudianteCarrera

- [x] (Medio) Evaluar `@EmbeddedId` con clave compuesta (`id_estudiante`, `id_carrera`) + `@MapsId` en vez de `id` propio + unique. Lo recomendaron en clase por ser más purista; la regla de no duplicar inscripciones es la misma.

## General

- [ ] (Bajo) Chequear que todas las entidades se construyan con builder. Hoy `Estudiante` se arma con setters en `EstudianteMapper.toEntity`.
- [x] (Medio) Unificar la validación de DTOs. Todos los request validan en el constructor compacto del record; se sacaron las anotaciones `javax.validation` y la dependencia del pom.

## Reporte (consigna 3)

- [ ] (Medio) `CarreraService.generarReporteCarreras` une y ordena en Java (`TreeMap`, `ClaveReporte` con `Comparable`, `MetricasAnio`), y la consigna pide resolverlo mayormente en JPQL. Opciones: una sola consulta que ya venga ordenada (egresados por año + total de inscriptos de la carrera), o dejar las dos consultas con `ORDER BY` y unirlas con un `HashMap` simple.

## Carrera
- [ ] (Bajo) `CarreraService.save` consulta `findByNombre` antes de insertar para detectar duplicados. Pasarlo al mismo criterio que `EstudianteCarreraService`: insertar directo y traducir la violación del unique (`ConstraintViolationException.getKind() == UNIQUE`) a `CarreraExistingException`. Ahorra una consulta y cubre dos altas simultáneas.
- [x] Pasar DTOs a Record
- [x] Hacer Implements
- [ ] Hacer Testing
- 