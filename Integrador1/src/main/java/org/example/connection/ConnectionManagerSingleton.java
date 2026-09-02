package org.example.connection;
import java.sql.*;

public class ConnectionManagerSingleton {

    private Connection conn;
    private static ConnectionManagerSingleton instance;

    private ConnectionManagerSingleton(){
        try{
            String url = "jdbc:mysql://localhost:3306/basedatosTP1";
            String user = "root";
            String password = "123";
            conn = DriverManager.getConnection(url, user, password);
        }catch (SQLException e){
            e.printStackTrace();
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
