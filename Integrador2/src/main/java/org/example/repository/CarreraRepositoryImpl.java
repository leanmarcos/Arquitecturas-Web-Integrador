package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
     *  Cuando un SELECT de JPQL trae varias columnas sueltas (c.nombre, COUNT(ce)), JPA no tiene una entidad donde
     *  meterlas y devuelve cada fila como Object[].
     * <p>
     * Por eso cada elemento de la lista es un arreglo {@code Object[]} con la siguiente estructura:
     * <ul>
     *   <li>{@code [0]} ({@link String}): Nombre de la carrera.</li>
     *   <li>{@code [1]} ({@link Long}): Cantidad de estudiantes inscriptos.</li>
     * </ul>
     *
     * @param em Instancia de {@link EntityManager} utilizada para ejecutar la consulta.
     * @return Lista de tuplas {@code Object[]} con el nombre y la cantidad de inscriptos por carrera.
     */
    @Override
    public List<Object[]> findCarreraWithEstudiantes(EntityManager em) {
        TypedQuery<Object[]> query = em.createQuery(
                """
                SELECT c.nombre, COUNT(ce) AS total_inscriptos 
                FROM Carrera c
                JOIN c.estudiantes ce
                GROUP BY c.nombre
                ORDER BY total_inscriptos DESC
                """, Object[].class
        );
        return query.getResultList();
    }
}
