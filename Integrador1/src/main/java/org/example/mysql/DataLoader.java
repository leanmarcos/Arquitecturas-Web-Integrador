package org.example.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.example.csv.CsvImporter;
import org.example.entity.Cliente;
import org.example.entity.Factura;
import org.example.entity.FacturaProducto;
import org.example.entity.Producto;


public class DataLoader {

    private final Connection connection;
    private final CsvImporter csvImporter = new CsvImporter();
    
    private final MySqlClienteDAO clienteDAO;
    private final MySqlProductoDAO productoDAO;
    private final MySqlFacturaDAO facturaDAO;
    private final MySqlFacturaProductoDAO facturaProductoDAO;

    public DataLoader(Connection connection) {
        this.connection = connection;
        clienteDAO = new MySqlClienteDAO(connection);
        productoDAO = new MySqlProductoDAO(connection);
        facturaDAO = new MySqlFacturaDAO(connection);
        facturaProductoDAO = new MySqlFacturaProductoDAO(connection);
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
        List<Cliente> clientes = csvImporter.importar(
            "/data/clientes.csv",
            row -> new Cliente(
                Integer.parseInt(row.get("idCliente")),
                row.get("nombre"),
                row.get("email")
            )
        );

        clienteDAO.insertBatch(clientes);
        return clientes.size();
    }
    
    private int loadProductos() throws SQLException {
        List<Producto> productos = csvImporter.importar(
            "/data/productos.csv",
            row -> new Producto(
                Integer.parseInt(row.get("idProducto")),
                row.get("nombre"),
                Float.valueOf(row.get("valor"))
            )
        );

        productoDAO.insertBatch(productos);
        return productos.size();
    }

    private int loadFacturas() throws SQLException {
        List<Factura> facturas = csvImporter.importar(
            "/data/facturas.csv",
            row -> new Factura(
                Integer.parseInt(row.get("idFactura")),
                Integer.parseInt(row.get("idCliente"))
            )
        );

        facturaDAO.insertBatch(facturas);
        return facturas.size();

    }

    private int loadFacturasProductos() throws SQLException {
        List<FacturaProducto> facturasProductos = csvImporter.importar(
            "/data/facturas-productos.csv",
            row -> new FacturaProducto(
                Integer.parseInt(row.get("idFactura")),
                Integer.parseInt(row.get("idProducto")),
                Integer.parseInt(row.get("cantidad"))
            )
        );

        facturaProductoDAO.insertBatch(facturasProductos);
        return facturasProductos.size();
    }
}