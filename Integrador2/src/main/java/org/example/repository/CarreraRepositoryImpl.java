package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.dto.CarreraInscriptosResponseDTO;
import org.example.model.Carrera;

import java.util.List;
import java.util.Optional;

public class CarreraRepositoryImpl implements CarreraRepository{
    @Override
    public Optional<Carrera> save(EntityManager em, Carrera carrera) {
        return Optional.empty();
    }

    @Override
    public List<Carrera> findAll(EntityManager em) {
        return List.of();
    }

    @Override
    public Optional<Carrera> findById(EntityManager em, Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Carrera> deleteById(EntityManager em, Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Carrera> findByNombre(EntityManager em, String nombre) {
        TypedQuery<Carrera> query = em.createQuery(
                "SELECT c FROM Carrera c WHERE c.nombre =:nombre", Carrera.class
        );
        query.setParameter("nombre", nombre);
        return query.getResultStream().findFirst();
    }

    /**
     * Recupera las carreras que poseen al menos un estudiante inscripto,
     * ordenadas por la cantidad total de inscriptos en orden descendente.
     * <p>
     * Cuando un SELECT de JPQL trae varias columnas sueltas (c.nombre, COUNT(ce)), no hay una entidad donde
     * guardarlas. En vez de recibir cada fila como {@code Object[]}, usamos una constructor expression
     * ({@code SELECT new ...}) para que JPA arme directamente el DTO. Ver ADR-constructor-expression.md.
     * <p>
     * Cada elemento de la lista es un {@link CarreraInscriptosResponseDTO} con:
     * <ul>
     *   <li>{@code nombre} ({@link String}): Nombre de la carrera.</li>
     *   <li>{@code totalInscriptos} ({@link Long}): Cantidad de estudiantes inscriptos.</li>
     * </ul>
     *
     * @param em Instancia de {@link EntityManager} utilizada para ejecutar la consulta.
     * @return Lista de {@link CarreraInscriptosResponseDTO} con el nombre y la cantidad de inscriptos por carrera.
     */
    @Override
    public List<CarreraInscriptosResponseDTO> findCarreraWithEstudiantes(EntityManager em) {
        TypedQuery<CarreraInscriptosResponseDTO> query = em.createQuery(
                """
                SELECT new org.example.dto.CarreraInscriptosResponseDTO(c.nombre, COUNT(ce))
                FROM Carrera c
                JOIN c.estudiantes ce
                GROUP BY c.nombre
                ORDER BY COUNT(ce) DESC
                """, CarreraInscriptosResponseDTO.class
        );
        return query.getResultList();
    }
}
