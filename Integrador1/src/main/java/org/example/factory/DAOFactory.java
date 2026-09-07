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
     * Devuelve la instancia de fábrica correspondiente al motor de base de datos indicado.
     *
     * @param db         motor de base de datos para el que se necesita la fábrica
     * @param connection conexión que usarán los DAOs creados por la fábrica
     * @return la {@link DAOFactory} concreta correspondiente a {@code db}
     * @throws IllegalArgumentException si {@code db} no tiene una fábrica soportada
     */
    public static DAOFactory getInstance(DbEngine db, Connection connection) {
           switch (db){
               case MYSQL:
                   return new MySqlDAOFactory(connection);
               default:
                   throw  new IllegalArgumentException("Motor no soportado: " + db);
           }
    }

    /**
     * @return el {@link ClienteDAO} de esta fábrica
     */
    public abstract ClienteDAO getClienteDAO();

    /**
     * @return el {@link FacturaDAO} de esta fábrica
     */
    public abstract FacturaDAO getFacturaDAO();

    /**
     * @return el {@link ProductoDAO} de esta fábrica
     */
    public abstract ProductoDAO getProductoDAO();

    /**
     * @return el {@link FacturaProductoDAO} de esta fábrica
     */
    public abstract FacturaProductoDAO getFacturaProductoDAO();

    /**
     * @return el {@link SchemaDAO} de esta fábrica
     */
    public abstract SchemaDAO getSchemaDAO();
}
