package org.example.dao;
import java.sql.SQLException;
import java.util.List;

import org.example.entity.Factura;

public interface FacturaDAO {
    void insertBatch(List<Factura> facturas) throws SQLException;
    void deleteFacturaById(int idFactura);

}
