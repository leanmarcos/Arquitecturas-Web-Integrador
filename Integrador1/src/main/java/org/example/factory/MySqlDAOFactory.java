package org.example.factory;

import org.example.dao.ClienteDAO;
import org.example.dao.FacturaDAO;
import org.example.dao.FacturaProductoDAO;
import org.example.dao.ProductoDAO;
import org.example.dao.SchemaDAO;
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

    private Connection connection;

    /**
     * Crea la fábrica de DAOs de MySQL asociada a la conexión dada.
     *
     * @param connection conexión JDBC que se inyectará en los DAOs creados por esta fábrica
     */
    public MySqlDAOFactory(Connection connection) {
        this.connection = connection;
    }

    /**
     * Devuelve el DAO de Cliente
     * @return una nueva instancia de {@link MySqlClienteDAO}
     */
    @Override
    public ClienteDAO getClienteDAO() {
        return new MySqlClienteDAO(connection);
    }

    /**
     * Devuelve el DAO de Factura
     * @return una nueva instancia de {@link MySqlFacturaDAO}
     */
    @Override
    public FacturaDAO getFacturaDAO() {
        return new MySqlFacturaDAO(connection);
    }

    /**
     * Devuelve el DAO de Producto
     * @return una nueva instancia de {@link MySqlProductoDAO}
     */
    @Override
    public ProductoDAO getProductoDAO() {

        return new MySqlProductoDAO(connection);
    }

    /**
     * Devuelve el DAO de FacturaProducto
     * @return una nueva instancia de {@link MySqlFacturaProductoDAO}
     */
    @Override
    public FacturaProductoDAO getFacturaProductoDAO() {
        return new MySqlFacturaProductoDAO(connection);
    }

    /**
     * Devuelve el DAO de Schema
     * @return una nueva instancia de {@link MySQLSchemaDAO}
     */
    @Override
    public SchemaDAO getSchemaDAO() {
        return new MySQLSchemaDAO(connection);
    }
}
