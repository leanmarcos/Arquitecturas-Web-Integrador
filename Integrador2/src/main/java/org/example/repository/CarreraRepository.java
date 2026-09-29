package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.dto.CarreraInscriptosResponseDTO;
import org.example.dto.FilaReporteDTO;
import org.example.model.Carrera;

import java.util.List;
import java.util.Optional;

public interface CarreraRepository{
    public Optional<Carrera> save(EntityManager em, Carrera carrera);

    public List<Carrera> findAll(EntityManager em);

    public Optional<Carrera>  findById(EntityManager em, Long id);

    public Optional<Carrera> deleteById(EntityManager em, Long id);

    Optional<Carrera> findByNombre(EntityManager em, String nombre);

    List<CarreraInscriptosResponseDTO> findCarreraWithEstudiantes(EntityManager em);

    /**
     * Obtiene la cantidad de estudiantes inscriptos agrupados por carrera y año de inscripción.
     *
     * @param em Instancia de {@link EntityManager} utilizada para ejecutar la consulta.
     * @return Lista de {@link FilaReporteDTO} con nombre de carrera, año de inscripción y cantidad.
     */
    List<FilaReporteDTO> findInscriptosPorAnio(EntityManager em);

    /**
     * Obtiene la cantidad de estudiantes egresados agrupados por carrera y año de graduación.
     * Solo considera inscripciones donde el año de graduación no sea nulo.
     *
     * @param em Instancia de {@link EntityManager} utilizada para ejecutar la consulta.
     * @return Lista de {@link FilaReporteDTO} con nombre de carrera, año de graduación y cantidad.
     */
    List<FilaReporteDTO> findEgresadosPorAnio(EntityManager em);
}
