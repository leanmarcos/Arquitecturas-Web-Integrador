package org.example.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.example.dao.FacturaProductoDAO;
import org.example.entity.FacturaProducto;

public class MySqlFacturaProductoDAO implements FacturaProductoDAO {
    private final Connection con;

    public MySqlFacturaProductoDAO(Connection con) {
        this.con = con;

    }

    @Override
    public void insertBatch(List<FacturaProducto> facturaProducto) {
        String sql = "INSERT INTO factura_producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (FacturaProducto fp : facturaProducto) {
                ps.setInt(1, fp.getIdFactura());
                ps.setInt(2, fp.getIdProducto());
                ps.setInt(3, fp.getCantidad());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
