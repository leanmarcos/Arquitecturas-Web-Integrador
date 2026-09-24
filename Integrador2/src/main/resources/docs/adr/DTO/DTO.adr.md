## DTO de entrada y salida con Validation en Java

Los **DTO (Data Transfer Object)** son objetos utilizados para definir qué datos entran y salen de nuestra API. Permiten separar el modelo interno de la aplicación (`Entity`) de la información que exponemos al cliente.

Una buena práctica es utilizar **DTOs diferentes para entrada y salida**, ya que los datos que necesitamos recibir no necesariamente son los mismos que queremos devolver.

### DTO de entrada

El **DTO de entrada** representa los datos que el cliente puede enviar a la API. Es el lugar adecuado para aplicar **validaciones sobre los datos recibidos**.

Por ejemplo:

```java
public record CrearEstudianteDTO(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,

    @NotBlank(message = "El apellido es obligatorio")
    String apellido,

    @Email(message = "El email no es válido")
    @NotBlank(message = "El email es obligatorio")
    String email,

    @Min(value = 18, message = "Debe ser mayor de edad")
    Integer edad
) {}
```

Las anotaciones pertenecen a **Jakarta Bean Validation** y permiten declarar las reglas directamente sobre los atributos del DTO.

Algunas validaciones habituales son:

| Anotación       | Función                                                       |
| --------------- | ------------------------------------------------------------- |
| `@NotNull`      | El valor no puede ser `null`                                  |
| `@NotBlank`     | El texto no puede ser `null`, vacío ni contener solo espacios |
| `@NotEmpty`     | Colección o texto no puede estar vacío                        |
| `@Email`        | Valida el formato de un email                                 |
| `@Size`         | Define un tamaño mínimo/máximo                                |
| `@Min` / `@Max` | Define límites numéricos                                      |
| `@Positive`     | El número debe ser positivo                                   |
| `@Pattern`      | Valida mediante una expresión regular                         |

El controlador puede activar estas validaciones utilizando `@Valid`:

```java
@PostMapping
public ResponseEntity<EstudianteResponseDTO> crear(
        @Valid @RequestBody CrearEstudianteDTO dto) {

    return ResponseEntity.ok(estudianteService.crear(dto));
}
```

El flujo sería:

```text
Cliente
   │
   ▼
CrearEstudianteDTO
   │
   │ @Valid
   ▼
Bean Validation
   │
   ├── ❌ Datos inválidos → Error 400
   │
   └── ✅ Datos válidos
            │
            ▼
         Service
```

### DTO de salida

El **DTO de salida** define qué información devolvemos al cliente.

Por ejemplo:

```java
public record EstudianteResponseDTO(
    Long id,
    String nombre,
    String apellido,
    String email
) {}
```

Notá que no necesariamente contiene todos los atributos de nuestra entidad:

```java
@Entity
public class Estudiante {

    @Id
    private Long id;

    private String nombre;
    private String apellido;
    private String email;

    private String password;
    private LocalDate fechaCreacion;
}
```

Podemos devolver:

```json
{
  "id": 15,
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@email.com"
}
```

sin exponer información interna como `password` o `fechaCreacion`.

### ¿Por qué separar entrada y salida?

Porque **entrada y salida tienen responsabilidades diferentes**.

```text
                 API
                  │
       ┌──────────┴──────────┐
       │                     │
       ▼                     ▼
  Input DTO             Output DTO
       │                     │
       │ Validation          │ Información
       ▼                     ▼
    Request               Response
       │                     │
       ▼                     ▲
     Entity ───── Service ───┘
```

Esto proporciona varias ventajas:

* **Seguridad:** evita exponer atributos internos.
* **Validación:** las reglas de entrada quedan claramente definidas.
* **Desacoplamiento:** la API no depende directamente de la estructura de la entidad.
* **Control del contrato:** podemos modificar la entidad sin necesariamente modificar la API.
* **Claridad:** queda explícito qué puede enviar y qué puede recibir el cliente.

En resumen, el **Input DTO controla y valida lo que entra**, mientras que el **Output DTO controla lo que sale**. La `Entity` queda como modelo interno de persistencia, evitando convertirla directamente en el contrato de nuestra API.
