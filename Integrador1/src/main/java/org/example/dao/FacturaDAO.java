package org.example.dao;

import org.example.entity.Factura;

import java.util.List;

public interface FacturaDAO {

    void insertAll(List<Factura> facturas);
    void deleteFacturaById(int idFactura);

}
