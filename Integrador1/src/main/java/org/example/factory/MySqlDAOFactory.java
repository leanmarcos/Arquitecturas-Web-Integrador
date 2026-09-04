package org.example.factory;

import org.example.dao.ClienteDAO;
import org.example.dao.FacturaDAO;
import org.example.dao.FacturaProductoDAO;
import org.example.dao.ProductoDAO;
import org.example.dao.SchemaDAO;
import org.example.connection.ConnectionManagerSingleton;
import org.example.mysql.MySqlClienteDAO;
import org.example.mysql.MySqlFacturaDAO;
import org.example.mysql.MySqlFacturaProductoDAO;
import org.example.mysql.MySqlProductoDAO;
import org.example.mysql.MySQLSchemaDAO;

import java.sql.Connection;

/**
 * Es la implementación de DAOFactory específica para MySQL.
 */
public class MySqlDAOFactory extends DAOFactory {

    private Connection conn;

    /**
     * Devuelve el DAO del factory
     * @param conn
     */
    public MySqlDAOFactory(Connection conn) {
        this.conn = conn;
    }

    /**
     * Devuelve el DAO de Cliente
     * @return
     */
    @Override
    public ClienteDAO getClienteDAO() {
        return new MySqlClienteDAO(conn);
    }

    /**
     * Devuelve el DAO de Factura
     * @return
     */
    @Override
    public FacturaDAO getFacturaDAO() {
        return new MySqlFacturaDAO(conn);
    }

    /**
     * Devuelve el DAO de Producto
     * @return
     */
    @Override
    public ProductoDAO getProductoDAO() {

        return new MySqlProductoDAO(conn);
    }

    /**
     * Devuelve el DAO de FacturaProducto
     * @return
     */
    @Override
    public FacturaProductoDAO getFacturaProductoDAO() {
        return new MySqlFacturaProductoDAO(conn);
    }

    /**
     * Devuelve el DAO de Schema
     * @return
     */
    @Override
    public SchemaDAO getSchemaDAO() {
        return new MySQLSchemaDAO(conn);
    }
}
