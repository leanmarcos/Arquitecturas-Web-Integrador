package org.example.dao;

import java.util.List;

import org.example.dto.ClienteFacturadoDTO;
import org.example.entity.Cliente;

/**
 * Define las operaciones de acceso y manipulación de los registros correspondientes a clientes.
 */
public interface ClienteDAO {

    /**
     * Inserta varios clientes en un solo lote.
     *
     * @param clientes lista de clientes a insertar
     */
    void insertAll(List<Cliente> clientes);

    /**
     * Obtiene los clientes junto con su gasto total, ordenados de mayor a menor facturación.
     *
     * @return una lista de {@link ClienteFacturadoDTO}, una por cada cliente con al menos una factura
     */
    List<ClienteFacturadoDTO> getClientsOrderedByBilling();
}
