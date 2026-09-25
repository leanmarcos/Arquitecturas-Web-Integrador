package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Carrera;

import java.util.List;
import java.util.Optional;

public interface CarreraRepository{
    public Optional<Carrera> save(EntityManager em, Carrera carrera);

    public List<Carrera> findAll(EntityManager em);

    public Optional<Carrera>  findById(EntityManager em, Long id);

    public Optional<Carrera> deleteById(EntityManager em, Long id);

    Optional<Carrera> findByNombre(EntityManager em, String nombre);

    List<Object[]> findCarreraWithEstudiantes(EntityManager em);
}
