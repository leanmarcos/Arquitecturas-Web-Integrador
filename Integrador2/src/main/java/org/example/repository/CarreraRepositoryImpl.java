package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.dto.CarreraInscriptosResponseDTO;
import org.example.dto.FilaReporteDTO;
import org.example.model.Carrera;
import java.util.List;
import java.util.Optional;

public class CarreraRepositoryImpl implements CarreraRepository{

    @Override
    public Optional<Carrera> save(EntityManager em, Carrera carrera) {
        em.persist(carrera);
        return Optional.of(carrera);
    }

    @Override
    public List<Carrera> findAll(EntityManager em) {
        TypedQuery<Carrera> query = em.createQuery("SELECT c FROM Carrera c", Carrera.class);
        return query.getResultList();
    }

    @Override
    public Optional<Carrera> findById(EntityManager em, Long id) {
        return Optional.ofNullable(em.find(Carrera.class, id));
    }

    @Override
    public Optional<Carrera> deleteById(EntityManager em, Long id) {
        Carrera carrera = em.find(Carrera.class, id);
        if (carrera == null) {
            return Optional.empty();
        }
        em.remove(carrera);
        return  Optional.of(carrera);
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
     * ({@code SELECT new ...}) para que JPA arme directamente el DTO. Ver Integrador2/docs/adr/utils/ADR-constructor-expression.md.
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
                ORDER BY COUNT(ce) DESC, c.nombre
                """, CarreraInscriptosResponseDTO.class
        );
        return query.getResultList();
    }

    /**
     * Obtiene la cantidad de inscriptos agrupados por carrera y año de inscripción.
     * Utiliza constructor expression en JPQL proyectando a {@link FilaReporteDTO}.
     */
    @Override
    public List<FilaReporteDTO> findInscriptosPorAnio(EntityManager em) {
        TypedQuery<FilaReporteDTO> query = em.createQuery(
                """
                SELECT new org.example.dto.FilaReporteDTO(c.nombre, ec.anioInscripcion, COUNT(ec))
                FROM Carrera c
                JOIN c.estudiantes ec
                GROUP BY c.nombre, ec.anioInscripcion
                """, FilaReporteDTO.class
        );
        return query.getResultList();
    }

    /**
     * Obtiene la cantidad de egresados agrupados por carrera y año de graduación.
     * Utiliza constructor expression en JPQL proyectando a {@link FilaReporteDTO}.
     */
    @Override
    public List<FilaReporteDTO> findEgresadosPorAnio(EntityManager em) {
        TypedQuery<FilaReporteDTO> query = em.createQuery(
                """
                SELECT new org.example.dto.FilaReporteDTO(c.nombre, ec.anioGraduacion, COUNT(ec))
                FROM Carrera c
                JOIN c.estudiantes ec
                WHERE ec.anioGraduacion IS NOT NULL
                GROUP BY c.nombre, ec.anioGraduacion
                """, FilaReporteDTO.class
        );
        return query.getResultList();
    }
}
