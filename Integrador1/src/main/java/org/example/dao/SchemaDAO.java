package org.example.dao;

import java.sql.SQLException;

/**
 * Clase diseñada para ejecutar cambios comunes en la BBDD
 */
public interface SchemaDAO {

    /**
     * Define una función para crear las tablas necesarias
     * @throws SQLException si no es posible crear la tabla
     */
    void createTables() throws SQLException;

    /**
     * Define una función para borrar las tablas necesarias
     * @throws SQLException si no es posible eliminar la tabla
     */
    void dropTables() throws SQLException;

}
