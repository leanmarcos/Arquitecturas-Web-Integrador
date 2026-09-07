package org.example.dto;

/**
 * DTO con el resultado del producto de mayor recaudación, usado en
 * {@link org.example.dao.ProductoDAO#getProductoMayorRecaudacion()}.
 *
 * @param idProducto  id del producto
 * @param nombre      nombre del producto
 * @param valor       valor unitario del producto
 * @param recaudacion suma de {@code cantidad * valor} de todas las líneas de factura en las que aparece el producto
 */
public record ProductoRecaudadoDTO(int idProducto, String nombre, float valor, float recaudacion) {}
