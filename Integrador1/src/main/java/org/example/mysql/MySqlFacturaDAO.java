package org.example.mysql;

import org.example.dao.FacturaDAO;
import org.example.entity.Factura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MySqlFacturaDAO implements FacturaDAO {

    private final Connection con;

    public MySqlFacturaDAO(Connection con) {
        this.con = con;
    }

    @Override
    public void insertFactura(List<Factura> facturas) { // Se cargan las factura en la DB
        String sql = "INSERT INTO factura(idCliente, idFactura) VALUES (?, ?)";
        try (var pstmt = con.prepareStatement(sql)) {
            for (Factura factura : facturas) {
                pstmt.setInt(1, factura.getIdCliente());
                pstmt.setInt(2, factura.getIdFactura());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Insertadas " + facturas.size() + " facturas.");
        } catch (Exception e) {
            System.err.println("Error al insertar facturas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFacturaById(int idFactura) { // Borra una factura por su ID
        String sql = "DELETE FROM factura WHERE factura.idFactura = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
