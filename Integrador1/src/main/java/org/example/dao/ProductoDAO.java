package org.example.dao;

import java.util.List;

import org.example.entity.Producto;

public interface ProductoDAO {
    void insert(String name, Float price);
    void update(String name, Float price);
    void delete(int id);
    Producto select(int id);
    List<Producto> getAll();
    void insertBatch(List<Producto> productos);
}
