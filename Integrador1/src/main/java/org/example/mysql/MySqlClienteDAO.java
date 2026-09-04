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
        String sql = "INSERT INTO cliente (idCliente, nombre, email) VALUES (?, ?, ?)";
        try (var pstmt = con.prepareStatement(sql)) {
            for (Cliente cliente : clientes) {
                pstmt.setInt(1, cliente.getIdCliente());
                pstmt.setString(2, cliente.getNombre());
                pstmt.setString(3, cliente.getEmail());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (Exception e) {
            throw new RuntimeException("Error al insertar clientes" ,e);
        }
    }

}
