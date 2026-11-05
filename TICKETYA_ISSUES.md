# TicketYa — backlog de issues por entregas

## Principio de independencia de entregas

Se plantea una organización en dos épicas principales, con independencia funcional entre sí:

- Entrega 1: foco en el núcleo del negocio: publicación, catálogo, compra, reserva y garantía de no doble venta.
- Entrega 2: foco en la capa completa del sistema: seguridad, trazabilidad, fila virtual, QR, recomendaciones, notificaciones, reportes, contenedores y pruebas finales.

Regla de dependencias:
- Las historias dentro de cada entrega pueden tener dependencias internas.
- Las entregas no deben depender una de la otra para desarrollarse.
- Única dependencia útil entre entregas: la integración de la API con OpenAPI puede ser validada con el contrato de la Entrega 1, pero no bloquea el resto de la Entrega 2.

---

# Épica 1 — Entrega 1: núcleo del sistema

## 1. ET1-01 — Definir alcance, actores y escenarios del core

User story:
- Como equipo, queremos especificar alcance, actores y flujos del núcleo del sistema, para alinear implementación y pruebas.

Criterios de aceptación / DoD:
- [ ] Se documentan roles de cliente, organizador, administrador y personal de acceso.
- [ ] Se describen los flujos principales: publicación, búsqueda, reserva, compra, cancelación y devolución.
- [ ] Se define el alcance de la Entrega 1 y el comportamiento esperado de la pasarela de pagos simulada.
- [ ] La especificación queda versionada y sirve de base para diseño, OpenAPI y tests.

Dependencias:
- Ninguna.

---

## 2. ET1-02 — Definir arquitectura de microservicios y flujo de compra

User story:
- Como equipo, queremos definir los microservicios y el flujo de compra, para evitar acoplamiento innecesario y facilitar la implementación aislada por servicio.

Criterios de aceptación / DoD:
- [ ] Se documentan servicios propuestos: Gateway, catálogo/eventos, lugares, reservas/pagos, entradas y acceso.
- [ ] Se incluye un diagrama de módulos con interfaces y sistemas externos.
- [ ] Se especifica el flujo de compra con reserva temporal, aprobación, rechazo y vencimiento.
- [ ] Se documentan responsabilidades y contratos REST para el core.

Dependencias:
- ET1-01

---

## 3. ET1-03 — Modelar eventos, lugares y sectores

User story:
- Como organizador, quiero definir lugares y sectores, para configurar la capacidad y los precios de cada evento.

Criterios de aceptación / DoD:
- [ ] Se modela cada lugar con ID, nombre, dirección, latitud/longitud y sectores.
- [ ] Se soportan sectores numerados y no numerados.
- [ ] Se valida la capacidad, filas/asientos y precios por sector.
- [ ] El modelo queda documentado en el diagrama ER del servicio de catálogo/lugares.

Dependencias:
- ET1-01, ET1-02

---

## 4. ET1-04 — Crear, configurar y publicar eventos

User story:
- Como organizador, quiero crear eventos en borrador, definir sectores y precios, y publicarlos, para ponerlos a la venta.

Criterios de aceptación / DoD:
- [ ] Un evento tiene ID, nombre, descripción, tipo, organizador, participantes, fecha/hora, lugar, estado.
- [ ] Un evento sale en borrador y solo puede publicarse cuando tiene datos y precios válidos.
- [ ] Administradores pueden suspender o cancelar eventos.
- [ ] Se validan estados: borrador, publicado, agotado, cancelado y finalizado.
- [ ] Se incluye prueba unitaria para las transiciones permitidas.

Dependencias:
- ET1-03

---

## 5. ET1-05 — Buscar y consultar eventos

User story:
- Como cliente, quiero buscar eventos por múltiples criterios, para encontrar una opción acorde a mi interés y disponibilidad.

Criterios de aceptación / DoD:
- [ ] Se soportan filtros por tipo, organizador, participante, ciudad y rango de fechas.
- [ ] Se ordenan resultados por fecha, precio o relevancia.
- [ ] Se manejan parámetros inválidos con errores HTTP claros.
- [ ] Existe prueba unitaria para filtros combinados y ordenamiento.

Dependencias:
- ET1-04

---

## 6. ET1-06 — Reservar temporalmente asientos o cupos

User story:
- Como cliente, quiero reservar un lugar mientras completo el pago, para evitar que otro lo tome antes.

Criterios de aceptación / DoD:
- [ ] Se reserva un asiento o un cupo por un tiempo configurable, con expiración.
- [ ] La reserva impide que el mismo lugar sea vendido a otro cliente mientras siga vigente.
- [ ] Si vence la reserva, el lugar se libera y queda disponible nuevamente.
- [ ] Se prueba idempotencia de la reserva y vencimiento.
- [ ] Se documenta el mecanismo anti-doble-venta elegido (bloqueos, control de versiones o unicidad).

Dependencias:
- ET1-03, ET1-04

---

## 7. ET1-07 — Comprar entradas y emitir QR único

User story:
- Como cliente, quiero pagar una reserva aprobada y recibir una entrada válida, para asegurar mi acceso al evento.

Criterios de aceptación / DoD:
- [ ] La compra se realiza sólo si la reserva es válida.
- [ ] El pago puede aprobarse o rechazarse y se liberan lugares según el resultado.
- [ ] Se emite una entrada con un QR único y detalle de compra.
- [ ] Se limita la cantidad de entradas por cliente por evento.
- [ ] Se prueba el caso de reintento y el caso de pago rechazado.

Dependencias:
- ET1-06

---

## 8. ET1-08 — Gestionar devoluciones y cancelación de eventos

User story:
- Como cliente o administrador, quiero gestionar devoluciones y cancelaciones, para mantener la consistencia de la operación y la disponibilidad del evento.

Criterios de aceptación / DoD:
- [ ] La devolución está permitida hasta 72 horas antes del evento.
- [ ] Un administrador aprueba o rechaza la devolución.
- [ ] Si se aprueba, se libera el asiento y se solicita reintegro al proveedor de pagos.
- [ ] Si un evento se cancela, todas las entradas vendidas pasan a reintegro.
- [ ] Existe prueba para casos de plazo, aprobación y cancelación.

Dependencias:
- ET1-07

---

## 9. ET1-09 — Documentar API REST con OpenAPI del core

User story:
- Como consumidor externo, quiero conocer los contratos de la API, para integrar correctamente la aplicación sin depender de su implementación interna.

Criterios de aceptación / DoD:
- [ ] Se documentan endpoints, métodos, parámetros, request/response y códigos de respuesta.
- [ ] El contrato queda en OpenAPI/Swagger y refleja la implementación del core.
- [ ] Se entregan ejemplos de JSON para caso exitoso y de error.
- [ ] La colección de requests valida los endpoints documentados.

Dependencias:
- ET1-04, ET1-05, ET1-06, ET1-07, ET1-08

---

## 10. ET1-10 — Probar lógica de negocio y la no doble venta bajo concurrencia

User story:
- Como equipo, queremos probar la lógica de negocio y las condiciones de concurrencia, para garantizar que no se venda el mismo asiento dos veces.

Criterios de aceptación / DoD:
- [ ] Hay pruebas unitarias de reserva, compra, búsqueda y devoluciones.
- [ ] Existe prueba concurrente sobre un mismo asiento o último cupo del sector.
- [ ] Verifica que solo una compra resulta exitosa.
- [ ] Se deja evidencia del mecanismo anti-doble-venta y del resultado esperado.

Dependencias:
- ET1-06, ET1-07

---

## 11. ET1-11 — Entregar README, datos de prueba y colección de requests

User story:
- Como desarrollador o evaluador, quiero levantar y probar el núcleo del sistema con instrucciones claras, para validar la entrega sin ambigüedad.

Criterios de aceptación / DoD:
- [ ] README explica cómo levantar el core, ejecutar tests y consultar Swagger.
- [ ] Se entregan datos de prueba y colecciones de requests para los endpoints principales.
- [ ] Se documentan pasos para probar compra con reserva temporal, pago rechazado y vencimiento.
- [ ] La entrega queda reproducible en un entorno local.

Dependencias:
- ET1-09, ET1-10

---

# Épica 2 — Entrega 2: sistema completo y operativo

## 12. ET2-01 — Gestionar la fila virtual para eventos de alta demanda

User story:
- Como cliente, quiero ingresar a una fila virtual con mi posición, para poder comprar de forma ordenada en eventos masivos.

Criterios de aceptación / DoD:
- [ ] Un evento puede marcarse como de alta demanda.
- [ ] El sistema asigna una posición al cliente y le informa su turno.
- [ ] Se habilita la compra por tandas o por orden de prioridad.
- [ ] La fila es consistente bajo reintentos y desconexiones.
- [ ] Hay pruebas de orden y concurrencia.

Dependencias:
- Ninguna dentro de la Entrega 2; se puede prototipar con contrato o servicio simulado.

---

## 13. ET2-02 — Validar entradas por QR al ingreso al evento

User story:
- Como personal de acceso, quiero validar la entrada de un cliente mediante QR, para asegurar que el acceso sea válido y único.

Criterios de aceptación / DoD:
- [ ] Se valida existencia, correspondencia con el evento y que la entrada no haya sido usada.
- [ ] Un QR usado dos veces no puede volver a ser validado.
- [ ] Los accesos quedan registrados con fecha, evento y resultado.
- [ ] Hay pruebas de QR válido, inválido, duplicado y concurrente.

Dependencias:
- ET1-07

---

## 14. ET2-03 — Registrar preferencias y sugerir eventos

User story:
- Como cliente, quiero registrar mis gustos y recibir recomendaciones, para descubrir eventos relevantes a mi perfil.

Criterios de aceptación / DoD:
- [ ] Se registran tipos de eventos, artistas favoritos y ciudades preferidas.
- [ ] Se generarán sugerencias usando historial y preferencias.
- [ ] Se elige una solución simplificada y se justifica.
- [ ] Se documenta el uso de una base NoSQL para estas preferencias.
- [ ] Hay pruebas de persistencia y recomendación.

Dependencias:
- ET1-05

---

## 15. ET2-04 — Enviar notificaciones de compra, cambios y artistas favoritos

User story:
- Como cliente, quiero recibir notificaciones por email o push, para estar informado sobre compras, cambios y eventos relacionados con mis artistas favoritos.

Criterios de aceptación / DoD:
- [ ] Hay notificación por compra confirmada y por evento cancelado o cambiado.
- [ ] Hay notificación cuando un artista favorito publica un evento en la ciudad del cliente.
- [ ] Los mensajes se disparan de forma asíncrona para no bloquear la compra.
- [ ] Se implementa timeout, reintento y manejo de fallo del proveedor.
- [ ] Hay pruebas de generación y fallo del emisor.

Dependencias:
- ET1-07, ET1-08, ET2-03

---

## 16. ET2-05 — Consultar reportes de ventas y ocupación por organizador

User story:
- Como organizador, quiero consultar reportes de mi evento, para controlar recaudación, ocupación y devoluciones por sector.

Criterios de aceptación / DoD:
- [ ] El reporte incluye entradas vendidas y devueltas por sector.
- [ ] Muestra porcentaje de ocupación y recaudación total.
- [ ] Se documenta la lógica de cálculo para devoluciones y cancelaciones.
- [ ] El organizador solo puede consultar sus propios eventos.
- [ ] Hay pruebas de los reportes con datos de ejemplo.

Dependencias:
- ET1-07, ET1-08

---

## 17. ET2-06 — Autenticación y autorización con JWT por roles

User story:
- Como usuario del sistema, quiero autenticarme y operar según mi rol, para acceder únicamente a las funciones permitidas.

Criterios de aceptación / DoD:
- [ ] Hay roles: cliente, organizador, administrador y personal de acceso.
- [ ] Los endpoints protegen acceso según el rol.
- [ ] El Gateway o los servicios validan JWT y permisos.
- [ ] Se prueba el acceso permitido y denegado.
- [ ] Se documentan reglas de seguridad y scopes mínimos.

Dependencias:
- ET1-09

---

## 18. ET2-07 — Definir consistencia y compensaciones entre servicios

User story:
- Como equipo, queremos definir la consistencia distribuida de la compra, para manejar fallos parciales sin vender más de una vez ni dejar estados inconsistentes.

Criterios de aceptación / DoD:
- [ ] Se documentan decisiones de consistencia fuerte versus eventual.
- [ ] Se define la estrategia de compensación y recuperación de estados parciales.
- [ ] Se consideran tiempoouts, reintentos y resultados de pago no confirmados.
- [ ] Se incluye una prueba de integración de fallo parcial con compensación.

Dependencias:
- ET1-07, ET1-08

---

## 19. ET2-08 — Implementar resiliencia, cache y fallbacks ante servicios externos

User story:
- Como operador del sistema, quiero que la aplicación sea resiliente ante fallos de proveedores externos y de red, para mantener disponibilidad y consistencia funcional.

Criterios de aceptación / DoD:
- [ ] Reintentos, timeouts y circuit breaker se documentan para pagos y notificaciones.
- [ ] Se define qué información se cachea y qué no se cachea.
- [ ] Se evita cachear disponibilidad de asientos o inventario sensible.
- [ ] Se prueban fallos y recuperaciones de proveedores externos.

Dependencias:
- ET1-05, ET1-07, ET2-04

---

## 20. ET2-09 — Registrar trazabilidad y logs críticos

User story:
- Como operador, quiero seguir cada operación crítica a través del sistema, para diagnosticar fallos, auditoría y diagnóstico de producción.

Criterios de aceptación / DoD:
- [ ] Cada petición HTTP queda registrada con endpoint, usuario, código y tiempo de procesamiento.
- [ ] Compra, devolución, cancelación e ingreso al evento quedan correlacionados con un identificador.
- [ ] No se registran datos sensibles del pago ni tokens completos.
- [ ] Se prueba una operación seguida a través de los microservicios.

Dependencias:
- ET1-07, ET1-08, ET2-02, ET2-07

---

## 21. ET2-10 — Contenerizar microservicios y orquestar con Docker Compose

User story:
- Como evaluador o desarrollador, quiero levantar todo el sistema con un único comando, para probarlo de forma reproducible.

Criterios de aceptación / DoD:
- [ ] Cada microservicio y base de datos tiene un contenedor separado y configuración documentada.
- [ ] Existe un docker-compose.yml que levanta el sistema completo.
- [ ] Se documentan requisitos, variables de entorno y health checks.
- [ ] No se exponen secretos ni credenciales en el repositorio.

Dependencias:
- ET1-02, ET2-06, ET2-07

---

## 22. ET2-11 — Pruebas de integración, seguridad y permisos

User story:
- Como equipo, queremos verificar que la integración entre microservicios y los permisos por rol sean correctos, para asegurar el comportamiento real del sistema.

Criterios de aceptación / DoD:
- [ ] Hay pruebas de integración para flujo principal de compra, reservas y acceso.
- [ ] Se validan endpoints por rol y se documenta la matriz de permisos.
- [ ] Se incluyen pruebas de token ausente, inválido y vencido.
- [ ] El entorno se ejecuta con contenedores o Testcontainers.

Dependencias:
- ET1-09, ET2-06, ET2-10

---

## 23. ET2-12 — Medir carga del flujo de compra y reportar resultados

User story:
- Como equipo, quiero medir el desempeño del flujo de compra bajo concurrencia, para evidenciar rendimiento y límites del sistema.

Criterios de aceptación / DoD:
- [ ] Existe un plan de carga reproducible para compra masiva.
- [ ] El informe documenta usuarios concurrentes, tiempos de respuesta, throughput y errores.
- [ ] Se demuestra que el sistema respeta la no doble venta bajo presión.
- [ ] Se incluyen resultados de la métrica y conclusiones.

Dependencias:
- ET1-10, ET2-10, ET2-11

---

## 24. ET2-13 — Documentar decisiones finales de diseño, tecnología y arquitectura

User story:
- Como equipo o evaluador, quiero contar con documentación final de diseño, para entender decisiones técnicas, arquitectura y ejecución del sistema.

Criterios de aceptación / DoD:
- [ ] Se actualiza el README principal con la estrategia global del sistema.
- [ ] Se justifican tecnología seleccionada, base NoSQL y patrones del diseño.
- [ ] Se documenta OpenAPI final y docker-compose.
- [ ] Se registran pruebas, resultados y supuestos de simplificación.

Dependencias:
- ET1-09, ET2-07, ET2-08, ET2-10, ET2-12

---

## 25. ET2-14 — Interfaz web opcional para flujos principales del cliente

User story:
- Como cliente, quiero una interfaz simple para buscar eventos, elegir sector y comprar, para probar el sistema sin invocar manualmente la API.

Criterios de aceptación / DoD:
- [ ] La interfaz consume el Gateway y permite buscar eventos, seleccionar sector y confirmar compra.
- [ ] La UI muestra mensajes de carga y errores claros.
- [ ] Se documentan pasos para ejecutarla junto con el backend.
- [ ] Es un plus opcional y no reemplaza la validación del backend.

Dependencias:
- ET1-05, ET1-07, ET1-09

---

# Orden sugerido de implementación

1. Entrega 1
   - ET1-01
   - ET1-02
   - ET1-03
   - ET1-04
   - ET1-05
   - ET1-06
   - ET1-07
   - ET1-08
   - ET1-09
   - ET1-10
   - ET1-11

2. Entrega 2
   - ET2-01
   - ET2-03
   - ET2-05
   - ET2-06
   - ET2-07
   - ET2-08
   - ET2-09
   - ET2-10
   - ET2-02
   - ET2-04
   - ET2-11
   - ET2-12
   - ET2-13
   - ET2-14 (opcional)

# Observación final sobre DoD y INVEST

- Cada issue es independiente y verificable.
- Cada issue se puede terminar en un tiempo acotado por un subgrupo.
- No se exige que la Entrega 2 espere a la Entrega 1 para empezar.
- El criterio de aceptación de cada historia está claramente definido con DoD.
- La separación en dos épicas evita acoplar la implementación de la funcionalidad completa con la entrega total del sistema.

