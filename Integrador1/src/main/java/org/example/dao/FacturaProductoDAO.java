package org.example.dao;

import org.example.entity.FacturaProducto;

import java.util.List;

/**
 * Define las operaciones de acceso y manipulación de los registros correspondientes a la relación factura-producto.
 */
public interface FacturaProductoDAO {

    /**
     * Inserta varias relaciones factura-producto en un solo lote.
     *
     * @param facturaProducto lista de relaciones factura-producto a insertar
     */
    void insertAll(List<FacturaProducto> facturaProducto);

}
