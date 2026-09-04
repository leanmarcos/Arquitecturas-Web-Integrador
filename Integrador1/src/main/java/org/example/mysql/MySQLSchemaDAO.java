package org.example.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.example.dao.SchemaDAO;

/**
 * Implementa SchemaDAO y contiene las operaciones necesarias para crear y eliminar las tablas del esquema de MySQL.
 */
public class MySQLSchemaDAO implements SchemaDAO {

    private final Connection connection;

    public MySQLSchemaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS cliente (
                        idCliente INT PRIMARY KEY,
                        nombre VARCHAR(500) NOT NULL,
                        email VARCHAR(150) NOT NULL
                    )
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS producto (
                        idProducto INT PRIMARY KEY,
                        nombre VARCHAR(45) NOT NULL,
                        valor DECIMAL(10, 2) NOT NULL
                    )
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS factura (
                        idFactura INT PRIMARY KEY,
                        idCliente INT NOT NULL,
                        CONSTRAINT fk_factura_cliente
                            FOREIGN KEY (idCliente) REFERENCES cliente(idCliente)
                    )
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS factura_producto (
                        idFactura INT NOT NULL,
                        idProducto INT NOT NULL,
                        cantidad INT NOT NULL,
                        PRIMARY KEY (idFactura, idProducto),
                        CONSTRAINT fk_detalle_factura
                            FOREIGN KEY (idFactura) REFERENCES factura(idFactura),
                        CONSTRAINT fk_detalle_producto
                            FOREIGN KEY (idProducto) REFERENCES producto(idProducto)
                    )
                    """);
        }
    }

    @Override
    public void dropTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS factura_producto");
            statement.executeUpdate("DROP TABLE IF EXISTS factura");
            statement.executeUpdate("DROP TABLE IF EXISTS producto");
            statement.executeUpdate("DROP TABLE IF EXISTS cliente");
        }
    }
}
