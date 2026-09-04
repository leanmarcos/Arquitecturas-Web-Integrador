package org.example.dao;

import java.sql.SQLException;
import java.util.List;

import org.example.entity.Cliente;

/**
 * Define las operaciones de acceso y manipulación de los registros correspondientes a clientes.
 */
public interface ClienteDAO {

    /**
     * Define función para agregar varios registros a la vez
     * @param clientes
     * @throws SQLException
     */
    void insertAll(List<Cliente> clientes) throws SQLException;
}
