# DataLoader — por qué usamos transacciones acá y no en cada DAO

Porque si cargábamos cada entidad (Cliente, Producto, Factura, FacturaProducto) con su propio `insertAll()`, cada uno 
insertando fila por fila con `executeUpdate()`. Teniamos dos problemas con eso:

- Lentitud: cada `executeUpdate()` es un viaje de ida y vuelta a MySQL. Con CSVs grandes, son miles de viajes en vez de 
uno solo.
- Inconsistencia si algo falla a mitad de camino: si se insertan 14 clientes uno por uno y da un error en el cliente 
  10, los primeros 9 ya quedaron guardados en la base. Y te quedaría con una carga parcial, sin darte cuenta.


### Batch: resuelve la lentitud

En vez de `executeUpdate()` por fila, usamos:

```
ps.addBatch();       // "anota el insert en una "lista de pendientes" en el objeto ps (PreparedStatement)
ps.executeBatch();   // "manda todos los que se anotaron, de una sola vez"
```

Esto baja de N viajes a la base (uno por fila) a 1 solo viaje con todo agrupado. Es una mejora de performance, nada más — no garantiza que si algo falla a mitad de camino, se revierta lo que ya se insertó.

### Transacción: resuelve la inconsistencia

Para que sea "todo o nada", hace falta una transacción real:

```
conn.setAutoCommit(false); // no confirmes nada todavía
...
conn.commit();              // confirmá todo junto
// o si algo falló:
conn.rollback();            // deshacé todo lo que se había hecho
```

_Aclaracion_: `addBatch/executeBatch` y transacción son dos problemas distintos que se resuelven con herramientas 
distintas: uno es velocidad, el otro es integridad de los datos. Se usan juntos, no es "uno reemplaza al otro".