package org.example.connection;
import java.sql.*;

/**
 * Responsabilidad: administrar la conexión con la base de datos.
 *
 * Esta clase centraliza los datos necesarios para conectarse a MySQL (URL, usuario y contraseña) y establece la conexión mediante JDBC. Implementa el patrón Singleton, garantizando que exista una única instancia del administrador de conexión durante la ejecución de la aplicación.
 *
 * Proporciona la conexión activa a las clases que necesitan realizar operaciones sobre la base de datos y también permite cerrarla de forma controlada.
 */
public class ConnectionManagerSingleton {

    private static final String URL = "jdbc:mysql://localhost:3306/db_integrador_tp1";
    private static final String USER = "app_user";
    private static final String PASSWORD = "12345";
    private Connection conn;
    private static ConnectionManagerSingleton instance;

    /**
     * Trata de conectarse a la BBDD usando el DriverManager, la URL, el user y la password
     *
     * @throws RuntimeException si no puede conectarse
     */
    private ConnectionManagerSingleton(){
        try{
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (SQLException e){
            throw new RuntimeException("No se pudo conectar a la base de datos", e);
        }
    }

    /**
     * Instancia su propia clase cumpliendo con el patón Singleton si es que no lo hizo antes
     * @return ConnectionManagerSingleton instance
     */
    public static ConnectionManagerSingleton getInstance() { // Instanciamos la conexion
        if (instance == null){
            instance = new ConnectionManagerSingleton();
        }
        return instance;
    }

    /**
     * Devuelve la conección a la BBDD
     * @return Connection conn
     */
   public Connection getConnection(){
        return conn;
   }

    /**
     * Cierra dando por finalizada la conección a la base de datos
     */
   public void closeConnection() {
        if (this.conn != null) {
            try {
                if (!this.conn.isClosed()) {
                    this.conn.close();
                    System.out.println("Conexión a la base de datos cerrada.");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerra la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
   }

}
