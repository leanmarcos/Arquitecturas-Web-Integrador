# ADR: Inyección por constructor en lugar de `@Autowired`

## Decisión

Las dependencias se declaran como `private final` y la clase se anota con `@RequiredArgsConstructor` (Lombok).

```java
@Service
@RequiredArgsConstructor
public class CarreraService {
    private final CarreraRepository repository;
}
```

Lombok genera un constructor solo con los campos `final` no inicializados, y Spring inyecta por ese constructor.
Un campo sin `final` (por ejemplo, uno auxiliar) queda afuera del constructor.

## Por qué no `@Autowired` sobre el campo

- El campo no puede ser `final`, así que la dependencia se puede reasignar.
- La clase no se puede instanciar sin Spring: en un test hay que levantar el contexto o usar reflection para setear el campo.
- Las dependencias quedan ocultas; con constructor se ven todas en un solo lugar.

## Consecuencias

- En los tests se puede crear la clase con `new` pasándole un mock.
- Si se olvida el `final`, el campo no entra al constructor y queda en `null` sin que falle la compilación.
- Aplica a controllers, services y cualquier otro componente con dependencias.
