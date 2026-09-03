package org.example.dao;

import java.sql.SQLException;
import java.util.List;

import org.example.entity.FacturaProducto;

public interface FacturaProductoDAO {
    void insertBatch(List<FacturaProducto> detalles) throws SQLException;
}
