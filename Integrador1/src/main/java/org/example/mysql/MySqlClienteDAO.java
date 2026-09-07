package org.example.mysql;

import org.example.dao.ClienteDAO;
import org.example.entity.Cliente;
import java.sql.Connection;
import java.util.List;

/**
 * Implementa ClienteDAO y contiene las operaciones SQL necesarias para gestionar los clientes almacenados en MySQL.
 *
 */
public class MySqlClienteDAO implements ClienteDAO {

    private final Connection con;

    public MySqlClienteDAO(Connection con) {
        this.con = con;
    }

    @Override
    public void insertAll(List<Cliente> clientes) {

        String query = "INSERT INTO cliente (idCliente, nombre, email) VALUES (?, ?, ?)";

        try (var stmt = con.prepareStatement(query)) {
            for (Cliente cliente : clientes) {
                stmt.setInt(1, cliente.getIdCliente());
                stmt.setString(2, cliente.getNombre());
                stmt.setString(3, cliente.getEmail());
                stmt.addBatch();
            }
            stmt.executeBatch();

        } catch (Exception e) {
            throw new RuntimeException("Error al insertar clientes" ,e);
        }
    }

}
