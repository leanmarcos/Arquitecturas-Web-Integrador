package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Carrera;

import java.util.List;
import java.util.Optional;

public class CarreraRepositoryImpl implements CarreraRepository{
    @Override
    public Optional<Carrera> save(Carrera carrera) {
        return Optional.empty();
    }

    @Override
    public List<Carrera> findAll() {
        return List.of();
    }

    @Override
    public Optional<Carrera> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Carrera> deleteById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Carrera> findByNombre(EntityManager em, String nombre) {
        TypedQuery<Carrera> query = em.createQuery(
                "SELECT c FROM Carrera c WHERE c.nombre =:nombre", Carrera.class
        );
        query.setParameter("nombre", nombre);
        return query.getResultStream().findFirst();
    }
}
