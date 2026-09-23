package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Estudiante;

public interface EstudianteRepository {
    Estudiante save(EntityManager em, Estudiante estudiante);
}
