package org.example.factory;

import org.example.dao.ClienteDAO;
import org.example.dao.FacturaDAO;
import org.example.dao.FacturaProductoDAO;
import org.example.dao.ProductoDAO;
import org.example.dao.SchemaDAO;

import java.sql.Connection;

/**
 * Clase abstracta que define la interfaz común para la creación y obtención de los diferentes DAO del sistema.
 *
 * También proporciona el mecanismo para seleccionar la fábrica correspondiente a partir de un DbEngine. Actualmente, cuando se selecciona MySQL, se crea una instancia de MySQLFactory.
 */
public abstract class DAOFactory{

    /**
     * Devuelve la instancia según el tipo de DB
     * @param db
     * @param connection
     * @return
     */
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
    public abstract SchemaDAO getSchemaDAO();
}
