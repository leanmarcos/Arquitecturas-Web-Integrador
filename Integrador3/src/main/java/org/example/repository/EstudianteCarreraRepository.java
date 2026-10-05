package org.example.repository;

import org.example.model.EstudianteCarrera;
import org.example.model.EstudianteCarreraId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteCarreraRepository extends JpaRepository<EstudianteCarrera, EstudianteCarreraId> {
}
