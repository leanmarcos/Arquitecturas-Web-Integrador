package org.example.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.example.entity.Producto;

public interface ProductoDAO {

    /**
     * Define función para agregar varios registros a la vez
     * @param productos
     */
    void insertAll(List<Producto> productos);

    /**
     * Define función para actualizar registro
     * @param name
     * @param price
     */
    void update(int id, String name, Float price);

    /**
     * Recibe un id por parámetros y elimina el registro con ese id
     * @param id
     */
    void delete(int id);

    /**
     * Define función para obtener un producto por su id
     * @param id
     * @return
     */
    Producto select(int id);

    /**
     * Define una función para obtener todos los productos
     * @return
     */
    List<Producto> getAll();

    void insertBatch(List<Producto> productos) throws SQLException;

    /**
     * Obtiene el producto con mayor recaudación total, entendiendo
     * recaudación como la suma de (cantidad vendida x valor) sobre
     * todas sus líneas de factura.
     * <p>
     * En caso de empate, el criterio de desempate queda a criterio
     * de la implementación, pero debe ser determinístico (documentado
     * en la clase concreta).
     *
     * @return el producto con mayor recaudación, o {@code null} si no hay ventas cargadas
     */
    Producto getProductoMayorRecaudacion();

}
