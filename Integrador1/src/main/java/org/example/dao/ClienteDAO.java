package org.example.dao;

import java.util.List;

import org.example.entity.Cliente;

public interface ClienteDAO {
    void insertBatch(List<Cliente> clientes);
}
