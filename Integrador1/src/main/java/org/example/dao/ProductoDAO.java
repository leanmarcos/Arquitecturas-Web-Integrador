package org.example.dao;

import java.util.List;

import org.example.dto.ProductoRecaudadoDTO;
import org.example.entity.Producto;

public interface ProductoDAO {

    /**
     * Inserta varios productos en un solo lote.
     *
     * @param productos lista de productos a insertar
     */
    void insertAll(List<Producto> productos);

    /**
     * Actualiza el nombre y el valor de un producto existente.
     *
     * @param id    id del producto a actualizar
     * @param name  nuevo nombre del producto
     * @param price nuevo valor del producto
     */
    void update(int id, String name, Float price);

    /**
     * Elimina el producto con el id indicado.
     *
     * @param id id del producto a eliminar
     */
    void delete(int id);

    /**
     * Busca un producto por su id.
     *
     * @param id id del producto a buscar
     * @return el {@link Producto} encontrado, o {@code null} si no existe
     */
    Producto select(int id);

    /**
     * Obtiene todos los productos cargados.
     *
     * @return una lista con todos los {@link Producto}, vacía si no hay ninguno
     */
    List<Producto> getAll();

    /**
     * Obtiene el producto con mayor recaudación total, entendiendo
     * recaudación como la suma de (cantidad vendida x valor) sobre
     * todas sus líneas de factura.
     * <p>
     * En caso de empate, el criterio de desempate queda a criterio
     * de la implementación, pero debe ser determinístico (documentado
     * en la clase concreta).
     *
     * @return el {@link ProductoRecaudadoDTO} con mayor recaudación, o {@code null} si no hay ventas cargadas
     */
    ProductoRecaudadoDTO getProductoMayorRecaudacion();

}
