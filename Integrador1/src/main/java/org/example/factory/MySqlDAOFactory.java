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

public class MySqlDAOFactory extends DAOFactory {

    private Connection conn;

    public MySqlDAOFactory(Connection conn) {
        this.conn = conn;
    }
    @Override
    public ClienteDAO getClienteDAO() {
        return new MySqlClienteDAO(conn);
    }

    @Override
    public FacturaDAO getFacturaDAO() {
        return new MySqlFacturaDAO(conn);
    }

    @Override
    public ProductoDAO getProductoDAO() {

        return new MySqlProductoDAO(conn);
    }

    @Override
    public FacturaProductoDAO getFacturaProductoDAO() {
        return new MySqlFacturaProductoDAO(conn);
    }

    @Override
    public SchemaDAO getSchemaDAO() {
        return new MySQLSchemaDAO(conn);
    }
}
