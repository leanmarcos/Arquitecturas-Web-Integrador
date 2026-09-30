# ¿Por qué el `DataLoader` deja afuera 5 inscripciones de `estudianteCarrera.csv`?

Es a propósito: son filas que rompen reglas del modelo, y cargarlas dejaría datos inconsistentes. Se rechazan y
quedan listadas en el `DataResult` con su motivo.

| id | Motivo | Regla |
|---|---|---|
| 82 | Repite estudiante y carrera de la fila 79 (DNI 64472668, carrera 7) | clave primaria (`id_estudiante`, `id_carrera`) |
| 51 | Graduación 2023 < inscripción 2024 | constructor de `EstudianteCarreraRequestDTO` (la base lo respalda con el `@Check`) |
| 52 | Graduación 2024 < inscripción 2025 | ídem |
| 71 | Graduación 2022 < inscripción 2023 | ídem |
| 78 | Graduación 2022 < inscripción 2024 | ídem |

**Por qué no corregirlas:** no hay forma de saber cuál es el dato correcto (¿año mal cargado o fila de más?).
Inventar un valor falsearía el reporte de inscriptos y egresados por año.
