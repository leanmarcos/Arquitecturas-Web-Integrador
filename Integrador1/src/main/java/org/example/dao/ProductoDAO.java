package org.example.dao;

import java.sql.SQLException;
import java.util.List;

import org.example.entity.Producto;

public interface ProductoDAO {

    void insertAll(List<Producto> productos);
    void update(String name, Float price);
    void delete(int id);
    Producto select(int id);
    List<Producto> getAll();
    void insertBatch(List<Producto> productos) throws SQLException;

}
