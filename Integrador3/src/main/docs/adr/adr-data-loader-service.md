# ADR: El `DataLoader` usa los services, no los controllers

## Decisión

El `DataLoader` carga los CSV llamando a los services. Antes de cada alta valida el DTO con el `Validator` de
Jakarta, así aplica las mismas anotaciones que la API (`@NotBlank`, `@AssertTrue`, etc.). Si alguna falla, lanza
`ValidationException`.

## Por qué no el controller

- **Llamar al método del controller no valida nada:** el `@Valid` y el `GlobalExceptionHandler` los ejecuta Spring
  MVC durante un request HTTP. Llamar al método directo equivale a llamar al service, con una capa de más.
- **Hacer requests HTTP a la propia app (como Postman):** hay que esperar a que el servidor esté levantado y
  conocer el puerto. Además es más lento y la carga depende de la red.
- **El loader no es un cliente:** inicializa la base de la propia app, así que le corresponde usar la capa de
  negocio. La API como cliente se prueba con Postman o con tests.
