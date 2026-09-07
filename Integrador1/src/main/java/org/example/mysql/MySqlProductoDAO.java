package org.example.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.dao.ProductoDAO;
import org.example.entity.Producto;

/**
 * Implementa FacturaProductoDAO y contiene las operaciones SQL necesarias para gestionar las relaciones entre facturas y productos.
 */

public class MySqlProductoDAO implements ProductoDAO {

    private final Connection conn;

    public MySqlProductoDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insertAll(List<Producto> productos) {
        String query = "INSERT INTO producto (idProducto, nombre, valor) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
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

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setFloat(2, price);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id){
        String query = "DELETE FROM producto WHERE idProducto = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Producto select(int id){
        String query = "SELECT * FROM producto WHERE idProducto = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Producto producto = mapProduct(rs);

            if (producto == null){
                return null;
            }
            return producto;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Producto> getAll(){
        String query = "SELECT * FROM producto";

        List<Producto> products = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Producto producto = mapProduct(rs);
                products.add(producto);
            }

            return products;

        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Producto mapProduct(ResultSet rs) throws SQLException {
        try {
            Producto p = new Producto(rs.getInt("idProducto"),
                    rs.getString("nombre"),
                    rs.getFloat("valor")
            );
            return p;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void insertBatch(List<Producto> productos) throws SQLException {
        String query = "INSERT INTO producto (idProducto, nombre, valor) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (Producto p : productos) {
                stmt.setInt(1, p.getIdProducto());
                stmt.setString(2, p.getNombre());
                stmt.setFloat(3, p.getValor());
                stmt.addBatch();
            }
            stmt.executeBatch();
            System.out.println("Insertados " + productos.size() + " productos.");
        }
    }

    /**
     * Obtiene el producto con mayor recaudación total.
     * <p>
     * La recaudación se calcula como la suma de (cantidad vendida x valor del producto) sobre
     * todas las líneas de factura en las que aparece ese producto (tabla {@code factura_producto}).
     * <p>
     * En caso de empate en recaudación entre dos o más productos, se devuelve el de menor {@code idProducto}
     *
     * @return El {@link Producto} con mayor recaudación, o {@code null}
     *         si no hay ventas cargadas (tabla {@code factura_producto} vacía)
     */
    @Override
    public Producto getProductoMayorRecaudacion() {
        String query = "SELECT p.idProducto, p.nombre, p.valor, " +
                        "SUM(fp.cantidad * p.valor) AS recaudacion " +
                        "FROM producto p " +
                        "JOIN factura_producto fp ON p.idProducto = fp.idProducto " +
                        "GROUP BY p.idProducto, p.nombre, p.valor " +
                        "ORDER BY recaudacion DESC, p.idProducto ASC " +
                        "LIMIT 1";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getFloat("valor")
                );
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
