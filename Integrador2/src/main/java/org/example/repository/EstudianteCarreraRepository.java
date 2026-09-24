package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.EstudianteCarrera;

public interface EstudianteCarreraRepository {
    EstudianteCarrera save(EntityManager em, EstudianteCarrera inscripcion);
}
