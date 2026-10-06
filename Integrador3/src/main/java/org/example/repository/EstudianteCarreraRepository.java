package org.example.repository;

import org.example.model.Estudiante;
import org.example.model.EstudianteCarrera;
import org.example.model.EstudianteCarreraId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstudianteCarreraRepository extends JpaRepository<EstudianteCarrera, EstudianteCarreraId> {

    @Query("""
        SELECT e
        FROM EstudianteCarrera ec
        JOIN ec.estudiante e
        JOIN ec.carrera c
        WHERE c.id = :idCarrera AND e.ciudadResidencia = :ciudadResidencia
        ORDER BY e.apellido, e.nombres
    """)
    List<Estudiante> findEstudiantesByCarreraAndCiudad(@Param("idCarrera") Long idCarrera,
                                                       @Param("ciudadResidencia") String ciudadResidencia);
}
