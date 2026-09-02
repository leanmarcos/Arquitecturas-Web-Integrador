package org.example.factory;

import org.example.dao.ClienteDAO;
import org.example.dao.FacturaDAO;
import org.example.dao.FacturaProductoDAO;
import org.example.dao.ProductoDAO;
import org.example.connection.ConnectionManagerSingleton;
import org.example.mysql.MySqlClienteDAO;

public class MySqlDAOFactory extends DAOFactory {


    @Override
    public ClienteDAO getClienteDAO() {
        return MySqlClienteDAO(ConnectionManagerSingleton.getInstance().getConnection())
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
