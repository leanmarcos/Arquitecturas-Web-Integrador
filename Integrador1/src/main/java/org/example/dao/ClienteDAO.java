package org.example.dao;

import java.sql.SQLException;
import java.util.List;

import org.example.entity.Cliente;

public interface ClienteDAO {
    void insertAll(List<Cliente> clientes) throws SQLException;
}
