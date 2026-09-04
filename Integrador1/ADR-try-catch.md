# Porque try-with-resourse en MySQLClienteDao y try-catch-finally en DataLoader?

`try-with-resources`  sirve cuando la clase que abre el recurso es la misma que debe cerrarlo definitivamente al 
terminar. `finally` sirve cuando el recurso no es tuyo — lo tomaste prestado, lo usás, y solo tenés que devolverlo a su estado original, no destruirlo.

### En `MySqlClienteDAO`

``` java
public void insertBatch(List<Cliente> clientes) throws SQLException {
    String sql = "INSERT INTO cliente (...) VALUES (?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        for (Cliente cliente : clientes) {
            stmt.setInt(1, cliente.getIdCliente());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getEmail());
            stmt.addBatch();
        }
        stmt.executeBatch();
    }
}
```
Acá el recurso que se abre dentro del método es el `PreparedStatement` (a veces también un `ResultSet`, si hacés un 
SELECT). El objeto que se abre y cierra  **no es la Connection** — es el PreparedStatement. Ese 
PreparedStatement:
- Nace y muere en este método. Nadie más lo necesita después.
- Es barato de recrear la próxima vez que llames al método.
- Si no lo cerrás, se te acumulan recursos del driver JDBC sin liberar (leak).

### En `DataLoader`

```java
public void cargarDatos() throws SQLException{
    boolean previousAutoCommit = connection.getAutoCommit();
    connection.setAutoCommit(false);
    try {
        // ... carga de clientes, productos, facturas, detalles
        connection.commit();
    } catch (SQLException | RuntimeException e) {
        connection.rollback();
        throw e;
    } finally {
        connection.setAutoCommit(previousAutoCommit);
    }
}
```
Aca el recurso es la `Connection`: 
- No nace en este método. Vino de afuera (Singleton, inyectada, lo que hayas definido).
- No debe morir acá. Después de que `cargarDatos()` termine, la conexión sigue viva — la va a seguir usando el resto de 
  la aplicación (otros DAOs, otras operaciones), hasta que en algún punto (al cerrar `Main`) alguien la cierre de verdad.
- Lo único que DataLoader "ensució" temporalmente fue el `autoCommit` (lo puso en false para controlar la transacción a 
  mano). Por eso el finally no cierra nada — solo restaura el estado (`previousAutoCommit`) para dejar la conexión como la encontró, lista para que la siga usando cualquier otro código.