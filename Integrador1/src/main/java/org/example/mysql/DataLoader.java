package org.example.mysql;

import org.example.csv.CsvImporter;
import org.example.entity.Cliente;
import org.example.entity.Factura;
import org.example.entity.FacturaProducto;
import org.example.entity.Producto;
import org.example.factory.DAOFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DataLoader {

    private final Connection connection;
    private final DAOFactory factory;
    private final CsvImporter csvImporter;

    public DataLoader(Connection connection, DAOFactory factory, CsvImporter csvImporter) {
        this.connection = connection;
        this.factory = factory;
        this.csvImporter = csvImporter;
    }

    public void loadAllData() throws SQLException {
        boolean previousAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);

        try {
            int clientes = loadClientes();
            int productos = loadProductos();
            int facturas = loadFacturas();
            int detalles = loadFacturasProductos();
            connection.commit();

            System.out.println("Clientes cargados: " + clientes);
            System.out.println("Productos cargados: " + productos);
            System.out.println("Facturas cargadas: " + facturas);
            System.out.println("Detalles de facturas cargados: " + detalles);
        } catch (SQLException | RuntimeException exception) {
            connection.rollback();
            throw exception;
        } finally {
            connection.setAutoCommit(previousAutoCommit);
        }
    }

    private int loadClientes() throws SQLException {
        List<Cliente> clientes = csvImporter.importar("/data/clientes.csv",
                row -> new Cliente(Integer.parseInt(row.get("idCliente")), row.get("nombre"), row.get("email")));

        String sql = "INSERT INTO cliente (idCliente, nombre, email) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), email = VALUES(email)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Cliente cliente : clientes) {
                statement.setInt(1, cliente.getIdCliente());
                statement.setString(2, cliente.getNombre());
                statement.setString(3, cliente.getEmail());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        return clientes.size();
    }

    private int loadProductos() throws SQLException {
        List<Producto> productos = csvImporter.importar("/data/productos.csv",
                row -> new Producto(Integer.parseInt(row.get("idProducto")), row.get("nombre"), Float.parseFloat(row.get("valor"))));

        String sql = "INSERT INTO producto (idProducto, nombre, valor) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), valor = VALUES(valor)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Producto producto : productos) {
                statement.setInt(1, producto.getIdProducto());
                statement.setString(2, producto.getNombre());
                statement.setFloat(3, producto.getValor());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        return productos.size();
    }

    private int loadFacturas() throws SQLException {
        List<Factura> facturas = csvImporter.importar("/data/facturas.csv",
                row -> new Factura(Integer.parseInt(row.get("idFactura")), Integer.parseInt(row.get("idCliente"))));

        String sql = "INSERT INTO factura (idFactura, idCliente) VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE idCliente = VALUES(idCliente)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Factura factura : facturas) {
                statement.setInt(1, factura.getIdFactura());
                statement.setInt(2, factura.getIdCliente());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        return facturas.size();
    }

    private int loadFacturasProductos() throws SQLException {
        List<FacturaProducto> detalles = csvImporter.importar("/data/facturas-productos.csv",
                row -> new FacturaProducto(Integer.parseInt(row.get("idFactura")), Integer.parseInt(row.get("idProducto")), Integer.parseInt(row.get("cantidad"))));

        String sql = "INSERT INTO factura_producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE cantidad = VALUES(cantidad)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (FacturaProducto detalle : detalles) {
                statement.setInt(1, detalle.getIdFactura());
                statement.setInt(2, detalle.getIdProducto());
                statement.setInt(3, detalle.getCantidad());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        return detalles.size();
    }
}
