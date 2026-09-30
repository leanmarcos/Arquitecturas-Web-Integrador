package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.EstudianteCarrera;

public class EstudianteCarreraRepositoryImpl implements EstudianteCarreraRepository{

    @Override
    public EstudianteCarrera save(EntityManager em, EstudianteCarrera inscripcion) {
        // la clave (estudiante, carrera) viene asignada: persist falla si ya existe, merge la pisaría
        em.persist(inscripcion);
        return inscripcion;
    }

}
