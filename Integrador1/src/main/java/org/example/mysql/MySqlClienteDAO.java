package org.example.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.example.dao.ClienteDAO;
import org.example.entity.Cliente;

public class MySqlClienteDAO implements ClienteDAO {
    //conecto a la base de datos
    private final Connection con;

    public MySqlClienteDAO(Connection con) {
        this.con = con;
    }
    @Override
    public void insertBatch(List<Cliente> clientes) throws SQLException {
        String sql = "INSERT INTO cliente (idCliente, nombre, email) VALUES (?, ?, ?)";
        try (var pstmt = con.prepareStatement(sql)) {
            for (Cliente cliente : clientes) {
                pstmt.setInt(1, cliente.getIdCliente());
                pstmt.setString(2, cliente.getNombre());
                pstmt.setString(3, cliente.getEmail());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Insertados " + clientes.size() + " clientes.");
        }
    }
}
