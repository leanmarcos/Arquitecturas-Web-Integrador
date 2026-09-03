package org.example.dao;

import org.example.entity.Producto;

import java.util.List;

public interface ProductoDAO {

    void insert(String name, Float price);
    void update(String name, Float price);
    void delete(int id);
    Producto select(int id);
    List<Producto> getAll();

}
