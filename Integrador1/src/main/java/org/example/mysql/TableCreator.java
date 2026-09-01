package org.example.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {

    private final Connection connection;

    public TableCreator(Connection connection) {
        this.connection = connection;
    }

    public void createAllTables() {

        try (Statement statement = connection.createStatement()) {

            String createClienteTable = "CREATE TABLE IF NOT EXISTS cliente (" + // Crea la tabla cliente
                                        "idCliente INT PRIMARY KEY, " +
                                        "nombre VARCHAR50(500), " +
                                        "email VARCHAR(150)" +
                                        ")";
            statement.executeUpdate(createClienteTable);

            String createProductoTable = "CREATE TABLE IF NOT EXISTS producto (" + // Crea la tabla producto
                                        "idProducto INT PRIMARY KEY, " +
                                        "nombre STRING VARCHAR(45), " +
                                        "valor FLOAT" +
                                        ")";
            statement.executeUpdate(createProductoTable);

            String createFacturaTable = "CREATE TABLE IF NOT EXISTS factura (" +  // Crea la tabla factura
                                        "idFactura INT PRIMARY KEY, " +
                                        "idCliente INT, " +
                                        "FOREIGN KEY (idCliente) REFERENCES cliente(idCliente) " +
                                        ")";
            statement.executeUpdate(createFacturaTable);

            String createFacturaProductoTable = "CREATE TABLE IF NOT EXISTS factura_producto (" + // Crea la tabla facturaProducto
                                                "idFactura INT, " +
                                                "idProducto INT, " +
                                                "cantidad INT, " +
                                                "PRIMARY KEY (idFactura, idProducto), " +
                                                "FOREIGN KEY (idFactura) REFERENCES factura(idFactura), " +
                                                "FOREIGN KEY (idProducto) REFERENCES producto(idProducto)" +
                                                ")";
            statement.executeUpdate(createFacturaProductoTable);

            System.out.println("Tablas creadas!");

        } catch (SQLException e) {
            System.err.print("Error al crear las tablas: " + e.getErrorCode());
            e.printStackTrace();
        }

    }

}
