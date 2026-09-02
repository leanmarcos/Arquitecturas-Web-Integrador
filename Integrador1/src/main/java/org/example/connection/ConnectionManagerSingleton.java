package org.example.connection;
import java.sql.*;

public class ConnectionManagerSingleton {

    private static final String URL = "jdbc:mysql://localhost:3306/basedatosTP1";
    private static final String USER = "root";
    private static final String PASSWORD = "123";

    private Connection conn;
    private static ConnectionManagerSingleton instance;

    private ConnectionManagerSingleton(){
        try{
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (SQLException e){
            throw new RuntimeException("No se pudo conectar a la base de datos", e);
        }
    }

    //instaciamos la conexion
    public static ConnectionManagerSingleton getInstance(){
        if(instance==null){
            instance = new ConnectionManagerSingleton();
        }
        return instance;
    }

   public Connection getConnection(){
        return conn;
   }

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
