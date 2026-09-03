package org.example.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.example.dao.ProductoDAO;
import org.example.entity.Producto;

public class MySqlProductoDAO implements ProductoDAO {

    private final Connection conn;

    public MySqlProductoDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(String name, Float price){
        String query = "INSERT INTO producto (nombre,precio) VALUES (?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, name);
            stmt.setFloat(2, price);
            stmt.execute();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(String name, Float price){
        String sql = "UPDATE productos SET nombre = ?, valor = ? WHERE idProducto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id){
        String query = "DELETE FROM productos WHERE idProducto = ?";
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
        try(PreparedStatement stms = conn.prepareStatement(query)){
            ResultSet rs = stms.executeQuery();
            Producto producto = mapProduct(rs);

            if(producto == null){
                return null;
            }
            return producto;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Producto> getAll(){
        String query = "SELECT * FROM producto";
        List<Producto> products = new ArrayList<>();

        try(PreparedStatement stms = conn.prepareStatement(query)){
            ResultSet rs = stms.executeQuery();

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
        try{
            Producto p = new Producto(rs.getInt("idProducto"),
                    rs.getString("nombre"),
                    rs.getFloat("valor")
            );
            return p;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void insertBatch(List<Producto> productos) {
        String sql = "INSERT INTO producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Producto p : productos) {
                ps.setInt(1, p.getIdProducto());
                ps.setString(2, p.getNombre());
                ps.setFloat(3, p.getValor());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
