package org.example.dao;

import org.example.entity.Factura;

import java.util.List;

/**
 * Define las operaciones de acceso y manipulación de los registros correspondientes a facturas.
 */
public interface FacturaDAO {

    /**
     * Inserta varias facturas en un solo lote.
     *
     * @param facturas lista de facturas a insertar
     */
    void insertAll(List<Factura> facturas);

    /**
     * Elimina la factura con el id indicado.
     *
     * @param idFactura id de la factura a eliminar
     */
    void deleteFacturaById(int idFactura);

}
