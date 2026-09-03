package org.example.dao;

import org.example.entity.Factura;

import java.util.List;

public interface FacturaDAO {

    void insertFactura(List<Factura> facturas);
    void deleteFacturaById(int idFactura);

}
