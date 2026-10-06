package org.example.repository;

import org.example.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    boolean existsByDni(Integer dni);

    Optional<Estudiante> findByDni(Integer dni);
}
