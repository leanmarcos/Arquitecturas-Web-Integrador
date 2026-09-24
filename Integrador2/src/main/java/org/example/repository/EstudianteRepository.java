package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Estudiante;

import java.util.List;
import java.util.Optional;

public interface EstudianteRepository {
    Estudiante save(EntityManager em, Estudiante estudiante);
    Optional<Estudiante> findByLu(EntityManager em, Long lu);
    List<Estudiante> findAllByGenero(EntityManager em, String genero);
}
