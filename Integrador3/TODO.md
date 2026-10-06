# TODO

## Estudiante

- [ ] (Bajo) Buscar por LU con `findByLu` / `existsByLu` en vez de `findById` / `existsById` heredados de `JpaRepository`, así el nombre dice por qué campo se busca. Generan la misma consulta porque la LU es el `@Id`. Hoy el único caso es `EstudianteService.findEstudianteByLu`, que ya usa `findByLu`; aplicar el mismo criterio en los próximos métodos.
