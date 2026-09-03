package org.example.dao;

import java.util.List;

import org.example.entity.FacturaProducto;

public interface FacturaProductoDAO {
    void insertBatch(List<FacturaProducto> facturaProducto);
}
