package org.example.factory;

import org.example.dao.ClienteDAO;
import org.example.dao.FacturaDAO;
import org.example.dao.FacturaProductoDAO;
import org.example.dao.ProductoDAO;

import java.sql.Connection;

public abstract class DAOFactory{

    public static DAOFactory getInstance(DbEngine db, Connection connection) {
           switch (db){
               case MYSQL:
                   return new MySqlDAOFactory(connection);
               default:
                   throw  new IllegalArgumentException("Motor no soportado: " + db);
           }
    }

    public abstract ClienteDAO getClienteDAO();
    public abstract FacturaDAO getFacturaDAO();
    public abstract ProductoDAO getProductoDAO();
    public abstract FacturaProductoDAO getFacturaProductoDAO();
}
