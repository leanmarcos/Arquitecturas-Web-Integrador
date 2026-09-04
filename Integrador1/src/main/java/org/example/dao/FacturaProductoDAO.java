package org.example.dao;

import org.example.entity.FacturaProducto;

import java.util.List;

/**
 * Define las operaciones de acceso y manipulación de los registros correspondientes a la relación factura-producto.
 */
public interface FacturaProductoDAO {

    /**
     * Define función para agregar varios registros a la vez
     * @param facturaProducto
     */
    void insertAll(List<FacturaProducto> facturaProducto);

}
