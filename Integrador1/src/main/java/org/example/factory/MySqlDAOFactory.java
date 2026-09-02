package org.example.factory;

import org.example.dao.ClienteDAO;
import org.example.dao.FacturaDAO;
import org.example.dao.FacturaProductoDAO;
import org.example.dao.ProductoDAO;
import org.example.connection.ConnectionManagerSingleton;
import org.example.mysql.MySqlClienteDAO;

import java.sql.Connection;

public class MySqlDAOFactory extends DAOFactory {

    private Connection conn;

    public MySqlDAOFactory() {
        this.conn = ConnectionManagerSingleton.getInstance().getConnection();
    }
    @Override
    public ClienteDAO getClienteDAO() {
        return new MySqlClienteDAO(conn);
    }

    @Override
    public FacturaDAO getFacturaDAO() {
        return null;
    }

    @Override
    public ProductoDAO getProductoDAO() {
        return null;
    }

    @Override
    public FacturaProductoDAO getFacturaProductoDAO() {
        return null;
    }
}
