package org.example.mysql;

import org.example.dao.FacturaDAO;
import org.example.entity.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementa ProductoDAO y contiene las operaciones SQL necesarias para gestionar los productos almacenados en MySQL.
 */

public class MySqlFacturaDAO implements FacturaDAO {

    private final Connection connection;

    public MySqlFacturaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertAll(List<Factura> facturas){
        String query = "INSERT INTO factura(idCliente, idFactura) VALUES (?, ?)";

        try (var stmt = connection.prepareStatement(query)) {
            for (Factura factura : facturas) {
                stmt.setInt(1, factura.getIdCliente());
                stmt.setInt(2, factura.getIdFactura());
                stmt.addBatch();
            }
            stmt.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar facturas", e);
        }
    }

    @Override
    public void deleteFacturaById(int id){
        String query = "DELETE FROM factura WHERE idFactura = ?";

        try(PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar factura con id " + id, e);
        }
    }
}
