package org.example.mysql;

import org.example.dao.ClienteDAO;
import org.example.dto.ClienteFacturadoDTO;
import org.example.entity.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa ClienteDAO y contiene las operaciones SQL necesarias para gestionar los clientes almacenados en MySQL.
 *
 */
public class MySqlClienteDAO implements ClienteDAO {

    private final Connection connection;

    public MySqlClienteDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertAll(List<Cliente> clientes) {

        String query = "INSERT INTO cliente (idCliente, nombre, email) VALUES (?, ?, ?)";

        try (var stmt = connection.prepareStatement(query)) {
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

    @Override
    public List<ClienteFacturadoDTO> getClientsOrderedByBilling() {
        String query = "SELECT c.idCliente, c.nombre, c.email, " +
                        "SUM(fp.cantidad * p.valor) AS gastoTotal " +
                        "FROM cliente c " +
                        "JOIN factura f ON f.idCliente = c.idCliente " +
                        "JOIN factura_producto fp ON fp.idFactura = f.idFactura " +
                        "JOIN producto p ON fp.idProducto = p.idProducto " +
                        "GROUP BY c.idCliente, c.nombre, c.email " +
                        "ORDER BY gastoTotal DESC, c.idCliente ASC ";

            List<ClienteFacturadoDTO> clients = new ArrayList<>();
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ClienteFacturadoDTO cl = new ClienteFacturadoDTO(
                            rs.getInt("idCliente"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getFloat("gastoTotal")
                    );
                    clients.add(cl);
                }
                return clients;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

}
