package org.example.mysql;

import java.sql.Connection;
import java.util.List;

import org.example.dao.FacturaDAO;
import org.example.entity.Factura;

public class MySqlFacturaDAO implements FacturaDAO {
    private final Connection con;

    public MySqlFacturaDAO(Connection con) {
        this.con = con;
    }
    @Override
    public void insertBatch(List<Factura> facturas) {
        // Implementación de la inserción de facturas en la base de datos MySQL
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
}
