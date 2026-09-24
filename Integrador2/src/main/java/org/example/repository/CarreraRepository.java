package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Carrera;

import java.util.List;
import java.util.Optional;

public interface CarreraRepository{
    public Optional<Carrera> save(Carrera carrera);

    public List<Carrera> findAll();

    public Optional<Carrera>  findById(Long id);

    public Optional<Carrera> deleteById(Long id);

    Optional<Carrera> findByNombre(EntityManager em, String nombre);
}
