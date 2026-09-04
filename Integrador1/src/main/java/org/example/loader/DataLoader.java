package org.example.loader;

import org.example.csv.CsvImporter;
import org.example.dao.ClienteDAO;
import org.example.dao.FacturaDAO;
import org.example.dao.FacturaProductoDAO;
import org.example.dao.ProductoDAO;
import org.example.entity.Cliente;
import org.example.entity.Factura;
import org.example.entity.FacturaProducto;
import org.example.entity.Producto;
import org.example.factory.DAOFactory;

import java.sql.Connection;
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

    /**Metodo que carga todos los csv a la base de datos
     *
     * Autocommit por default en true = Cada sentencia SQL que se ejecuta se confirma sola, inmediatamente, apenas
     * termina. Cada INSERT, apenas corre, ya queda guardado en la base — no hay "vuelta atrás" para esa sentencia en
     * particular.
     *
     * Autocommit en false = Agrupa todo lo que se ejecute de aca en adelante hasta que se pida que se cierre con
     * commit()
     *
     * @throws SQLException
     */
    public DataResult loadAllData() throws SQLException {
        DataResult resultado = new DataResult();
        boolean previousAutoCommit = connection.getAutoCommit(); //por default en true
        connection.setAutoCommit(false); //empieza la transaccion

        try {
            resultado.setClientes(loadClientes());
            resultado.setProductos(loadProductos());
            resultado.setFacturas(loadFacturas());
            resultado.setDetalles(loadFacturasProductos());
            connection.commit(); //se cierra la transaccion

            return resultado;

        } catch (SQLException | RuntimeException exception) {
            connection.rollback(); //deshace todo lo que se hizo si algo salio mal
            throw exception;
        } finally {
            connection.setAutoCommit(previousAutoCommit);
        }
    }

    private int loadClientes() throws SQLException {
        List<Cliente> clientes = csvImporter.importar("/data/clientes.csv",
                row -> new Cliente(Integer.parseInt(row.get("idCliente")), row.get("nombre"), row.get("email")));
        ClienteDAO clienteDAO = factory.getClienteDAO();
        clienteDAO.insertAll(clientes);
        return clientes.size();
    }

    private int loadProductos() throws SQLException {
        List<Producto> productos = csvImporter.importar("/data/productos.csv",
                row -> new Producto(Integer.parseInt(row.get("idProducto")), row.get("nombre"), Float.parseFloat(row.get("valor"))));
        ProductoDAO productoDAO = factory.getProductoDAO();
        productoDAO.insertAll(productos);
        return productos.size();
    }

    private int loadFacturas() throws SQLException {
        List<Factura> facturas = csvImporter.importar("/data/facturas.csv",
                row -> new Factura(Integer.parseInt(row.get("idFactura")), Integer.parseInt(row.get("idCliente"))));
        FacturaDAO facturaDAO = factory.getFacturaDAO();
        facturaDAO.insertAll(facturas);
        return facturas.size();
    }

    private int loadFacturasProductos() throws SQLException {
        List<FacturaProducto> detalles = csvImporter.importar("/data/facturas-productos.csv",
                row -> new FacturaProducto(Integer.parseInt(row.get("idFactura")), Integer.parseInt(row.get("idProducto")), Integer.parseInt(row.get("cantidad"))));
        FacturaProductoDAO facturaProductoDAO = factory.getFacturaProductoDAO();
        facturaProductoDAO.insertAll(detalles);
        return detalles.size();
    }
}
