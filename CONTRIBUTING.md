# Cómo nos conviene trabajar

Esto son sugerencias para trabajar en equipo sin pisarnos, no reglas fijas. Si algo no nos sirve, lo cambiamos
entre todos.

## La idea general

La idea es que todos hagamos un poco de todo: modelado, consultas y reporte. Para que eso funcione sin que
cinco personas terminen editando el mismo archivo el mismo día, ayuda separar el trabajo por archivo y por
momento, y no por persona fija.

Lo primero que conviene hacer es cerrar el modelo. Una o dos personas dejan las entidades listas en `main`, y
recién después se reparten las consultas. Si todos tocan una entidad mientras alguien le cambia el mapeo, los
conflictos no paran.

Con las consultas pasa algo parecido: si dos tickets tocan el mismo DAO, lo mejor es que los haga la misma
persona o que los hagan juntos en una tarde. Para que todos pasen por todo, se puede rotar: en una segunda
ronda corta cada uno agarra algo distinto de lo que hizo en la primera.

## Tickets

Sirve armar una épica con sub-issues de GitHub, que muestran solos el progreso, y un GitHub Project con las
columnas `To Do`, `In Progress`, `Review` y `Done`. Antes de arrancar un ticket, está bueno asignárselo y
pasarlo a `In Progress`, para que el resto sepa quién está con qué.

## Ramas

Una forma cómoda de nombrarlas es `tipo/número-de-issue-descripcion-corta`, por ejemplo:

```
feat/45-alta-estudiante
fix/45-null-pointer-alta
docs/42-diagrama-der
```

Los tipos más comunes son `feat`, `fix`, `docs`, `refactor` y `chore`. Conviene usar el número del issue de
GitHub para que la rama, el commit y el PR queden vinculados al ticket, y escribir la descripción en minúsculas
y con guiones.

## Commits y pull requests

Un ticket, una rama, un PR: así los cambios son chicos y fáciles de revisar. Para los commits ayuda arrancar con
el tipo, como en `feat: agregar alta de estudiante`. Si en la descripción del PR se escribe `Closes #45`, el
issue se cierra solo al mergear.

Antes de mergear a `main`, está bueno que otra persona del equipo revise el PR. Y antes de empezar cada sesión
de código, un `git pull` de `main` evita muchos dolores de cabeza. Si vas a tocar un archivo compartido,
sobre todo las entidades, un aviso en el grupo ayuda.
