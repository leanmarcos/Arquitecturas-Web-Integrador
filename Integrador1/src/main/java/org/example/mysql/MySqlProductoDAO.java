package org.example.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.dao.ProductoDAO;
import org.example.dto.ProductoRecaudadoDTO;
import org.example.entity.Producto;

/**
 * Implementa ProductoDAO y contiene las operaciones SQL necesarias para gestionar los productos almacenados en MySQL.
 */

public class MySqlProductoDAO implements ProductoDAO {

    private final Connection connection;

    public MySqlProductoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertAll(List<Producto> productos) {
        String query = "INSERT INTO producto (idProducto, nombre, valor) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (Producto producto : productos) {
                stmt.setInt(1, producto.getIdProducto());
                stmt.setString(2, producto.getNombre());
                stmt.setFloat(3, producto.getValor());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar productos", e);
        }
    }

    @Override
    public void update(int id, String name, Float price){
        String query = "UPDATE producto SET nombre = ?, valor = ? WHERE idProducto = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setFloat(2, price);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto con id " + id, e);
        }
    }

    @Override
    public void delete(int id){
        String query = "DELETE FROM producto WHERE idProducto = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto con id " + id, e);
        }
    }

    @Override
    public Producto select(int id){
        String query = "SELECT * FROM producto WHERE idProducto = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapProduct(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto con id " + id, e);
        }
    }

    @Override
    public List<Producto> getAll(){
        String query = "SELECT * FROM producto";

        List<Producto> products = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(mapProduct(rs));
            }

            return products;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener productos", e);
        }
    }

    /**
     * Mapea la fila actual de un {@link ResultSet} a un {@link Producto}.
     *
     * @param rs result set ya posicionado en una fila válida (se asume que ya se llamó a {@code rs.next()})
     * @return el {@link Producto} construido a partir de la fila actual
     * @throws SQLException si falla la lectura de alguna columna
     */
    private Producto mapProduct(ResultSet rs) throws SQLException {
        return new Producto(rs.getInt("idProducto"),
                rs.getString("nombre"),
                rs.getFloat("valor")
        );
    }

    /**
     * Obtiene el producto con mayor recaudación total.
     * <p>
     * La recaudación se calcula como la suma de (cantidad vendida x valor del producto) sobre
     * todas las líneas de factura en las que aparece ese producto (tabla {@code factura_producto}).
     * <p>
     * En caso de empate en recaudación entre dos o más productos, se devuelve el de menor {@code idProducto}
     *
     * @return El {@link ProductoRecaudadoDTO} con mayor recaudación, o {@code null}
     *         si no hay ventas cargadas (tabla {@code factura_producto} vacía)
     */
    @Override
    public ProductoRecaudadoDTO getProductoMayorRecaudacion() {
        String query = "SELECT p.idProducto, p.nombre, p.valor, " +
                        "SUM(fp.cantidad * p.valor) AS recaudacion " +
                        "FROM producto p " +
                        "JOIN factura_producto fp ON p.idProducto = fp.idProducto " +
                        "GROUP BY p.idProducto, p.nombre, p.valor " +
                        "ORDER BY recaudacion DESC, p.idProducto ASC " +
                        "LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new ProductoRecaudadoDTO(
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getFloat("valor"),
                        rs.getFloat("recaudacion")
                );
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
