package org.example.dao;

import org.example.entity.Factura;

import java.util.List;

/**
 * Define las operaciones de acceso y manipulación de los registros correspondientes a facturas.
 */
public interface FacturaDAO {

    /**
     * Define función para agregar varios registros a la vez
     * @param facturas
     */
    void insertAll(List<Factura> facturas);

    /**
     * Recibe un id por parámetros y elimina el registro con ese id
     * @param idFactura
     */
    void deleteFacturaById(int idFactura);

}
