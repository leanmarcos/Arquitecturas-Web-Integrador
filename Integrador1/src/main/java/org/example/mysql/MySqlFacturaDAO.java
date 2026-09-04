package org.example.mysql;

import org.example.dao.FacturaDAO;
import org.example.entity.Factura;

import java.sql.Connection;
import java.util.List;

public class MySqlFacturaDAO implements FacturaDAO {
    private final Connection connection;

    public MySqlFacturaDAO(Connection connection) {
        this.connection = connection;
    }


    public void insertFactura(List<Factura> facturas) {
        // Implementación de la inserción de facturas en la base de datos MySQL
        String sql = "INSERT INTO factura(idCliente, idFactura) VALUES (?, ?)";
        try (var pstmt = connection.prepareStatement(sql)) {
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
    public void deleteFacturaById(int idFactura) {
    }
}
