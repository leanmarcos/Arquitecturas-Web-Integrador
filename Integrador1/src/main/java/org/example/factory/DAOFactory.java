package org.example.factory;

import org.example.dao.ClienteDAO;
import org.example.dao.FacturaDAO;
import org.example.dao.FacturaProductoDAO;
import org.example.dao.ProductoDAO;

public abstract class DAOFactory{

    private static final int mysql = 1;
    private static DAOFactory instance;

    public static DAOFactory getInstance(int db){
       if (instance==null){
           switch (db){
               case mysql:
                   instance = new MySqlDAOFactory();
                   break;
               default:
                   return null;
           }
       }
       return instance;
    }

    public abstract ClienteDAO getClienteDAO();
    public abstract FacturaDAO getFacturaDAO();
    public abstract ProductoDAO getProductoDAO();
    public abstract FacturaProductoDAO getFacturaProductoDAO();

}
