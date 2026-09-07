package org.example.mysql;

import org.example.dao.FacturaProductoDAO;
import org.example.entity.FacturaProducto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementa FacturaProductoDAO y contiene las operaciones SQL necesarias para gestionar las relaciones entre facturas y productos almacenadas en MySQL.
 */

public class MySqlFacturaProductoDAO implements FacturaProductoDAO {

    private final Connection connection;

    public MySqlFacturaProductoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertAll(List<FacturaProducto> facturaProducto) {
        String query = "INSERT INTO factura_producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (FacturaProducto fp : facturaProducto) {
                stmt.setInt(1, fp.getIdFactura());
                stmt.setInt(2, fp.getIdProducto());
                stmt.setInt(3, fp.getCantidad());
                stmt.addBatch();
            }
            stmt.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar detalles de factura", e);
        }
    }
}
