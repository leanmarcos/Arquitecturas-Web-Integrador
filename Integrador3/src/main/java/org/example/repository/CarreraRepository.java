package org.example.repository;

import org.example.model.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    boolean existsByNombre(String nombre);

    Optional<Carrera> findByNombre(String nombre);
}
