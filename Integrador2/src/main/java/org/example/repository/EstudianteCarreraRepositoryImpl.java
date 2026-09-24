package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.EstudianteCarrera;

public class EstudianteCarreraRepositoryImpl implements EstudianteCarreraRepository{
    @Override
    public EstudianteCarrera save(EntityManager em, EstudianteCarrera inscripcion) {
        if (inscripcion.getId() == null) {
            em.persist(inscripcion);
        } else {
            inscripcion = em.merge(inscripcion);
        }
        return inscripcion;
    }
}
