# TODO

## Estudiante

- [ ] (Bajo) Buscar por LU con `findByLu` / `existsByLu` en vez de `findById` / `existsById` heredados de `JpaRepository`, así el nombre dice por qué campo se busca. Generan la misma consulta porque la LU es el `@Id`. Hoy el único caso es `EstudianteService.findEstudianteByLu`, que ya usa `findByLu`; aplicar el mismo criterio en los próximos métodos.

## EstudianteCarrera

- [ ] (Medio) Tests de integración de `POST /inscripciones` (el DNI viaja en el body): alta OK (201), estudiante inexistente (404), carrera inexistente (404), inscripción repetida (409) y graduación anterior a la inscripción (400).
