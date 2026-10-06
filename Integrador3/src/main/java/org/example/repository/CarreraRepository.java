package org.example.repository;

import org.example.dto.CarreraInscriptosResponseDTO;
import org.example.model.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    boolean existsByNombre(String nombre);

    Optional<Carrera> findByNombre(String nombre);

    @Query(
            """
              SELECT new org.example.dto.CarreraInscriptosResponseDTO(c.nombre, COUNT(ec))
              FROM Carrera c
              JOIN c.estudiantes ec
              GROUP BY c
              ORDER BY COUNT(ec) DESC, c.nombre
            """
    )
    List<CarreraInscriptosResponseDTO> findAllOrderedByInscriptos();
}
