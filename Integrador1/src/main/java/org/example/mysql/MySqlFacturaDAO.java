package org.example.mysql;

import org.example.dao.FacturaDAO;
import org.example.entity.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MySqlFacturaDAO implements FacturaDAO {
    private final Connection connection;

    public MySqlFacturaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertAll(List<Factura> facturas){
        String sql = "INSERT INTO factura(idCliente, idFactura) VALUES (?, ?)";
        try (var pstmt = connection.prepareStatement(sql)) {
            for (Factura factura : facturas) {
                pstmt.setInt(1, factura.getIdCliente());
                pstmt.setInt(2, factura.getIdFactura());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar facturas", e);
        }
    }

    @Override
    public void deleteFacturaById(int id){
        String query = "DELETE FROM  factura WHERE idFactura = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar factura con id " + id, e);
        }
    }
}
