# TODO

## Estudiante

- [x] (Bajo) `findAllByGenero` distingue mayúsculas: "Femenino" ≠ "femenino". Resuelto pasando `genero` a enum (`EstudianteGenero.from`).

## EstudianteCarrera

- [ ] (Medio) Evaluar `@EmbeddedId` con clave compuesta (`id_estudiante`, `id_carrera`) + `@MapsId` en vez de `id` propio + unique. Lo recomendaron en clase por ser más purista; la regla de no duplicar inscripciones es la misma.

## General

- [ ] (Bajo) Chequear que todas las entidades se construyan con builder. Hoy `Estudiante` se arma con setters en `EstudianteMapper.toEntity`.
- [ ] (Medio) Unificar la validación de DTOs. Los de Carrera usan anotaciones `javax.validation` que no se ejecutan (solo está la API en el pom, sin implementación ni llamada al `Validator`); los de Estudiante validan en el constructor compacto del record. Elegir uno: pasar todos al constructor compacto, o configurar Bean Validation (`jakarta.validation` + hibernate-validator) para todos.

## Carrera
- [ ] (Bajo) `CarreraService.save` consulta `findByNombre` antes de insertar para detectar duplicados. Pasarlo al mismo criterio que `EstudianteCarreraService`: insertar directo y traducir la violación del unique (`ConstraintViolationException.getKind() == UNIQUE`) a `CarreraExistingException`. Ahorra una consulta y cubre dos altas simultáneas.
- [x] Pasar DTOs a Record
- [ ] Hacer Implements
- [ ] Hacer Testing
- 