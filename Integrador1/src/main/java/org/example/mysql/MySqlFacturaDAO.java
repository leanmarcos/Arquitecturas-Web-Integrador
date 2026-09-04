package org.example.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.example.dao.FacturaDAO;
import org.example.entity.Factura;

public class MySqlFacturaDAO implements FacturaDAO {
    private final Connection connection;

    public MySqlFacturaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertBatch(List<Factura> facturas) throws SQLException {
        String sql = "INSERT INTO factura(idCliente, idFactura) VALUES (?, ?)";
        try (var pstmt = connection.prepareStatement(sql)) {
            for (Factura factura : facturas) {
                pstmt.setInt(1, factura.getIdCliente());
                pstmt.setInt(2, factura.getIdFactura());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("Insertadas " + facturas.size() + " facturas.");
        }
    }

    @Override
    public void deleteFacturaById(int id){
        String query = "DELETE FROM  factura WHERE idFactura = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
