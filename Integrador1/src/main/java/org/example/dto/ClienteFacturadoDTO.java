package org.example.dto;

/**
 * DTO con el resultado de un cliente y su gasto total, usado en
 * {@link org.example.dao.ClienteDAO#getClientsOrderedByBilling()}.
 *
 * @param idCliente  id del cliente
 * @param nombre     nombre del cliente
 * @param email      email del cliente
 * @param gastoTotal suma de {@code cantidad * valor} de todos los productos facturados a este cliente
 */
public record ClienteFacturadoDTO(int idCliente, String nombre, String email, float gastoTotal) {}
