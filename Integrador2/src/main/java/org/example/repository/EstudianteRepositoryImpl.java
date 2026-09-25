package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Estudiante;

import java.util.List;
import java.util.Optional;

public class EstudianteRepositoryImpl implements EstudianteRepository{
    @Override
    public Estudiante save(EntityManager em, Estudiante estudiante) {
        if(estudiante.getLu() == null){
            em.persist(estudiante);
        }else{
            estudiante = em.merge(estudiante);
        }
        return estudiante;
    }

    @Override
    public Optional<Estudiante> findByLu(EntityManager em, Long lu) {
        return Optional.ofNullable(em.find(Estudiante.class, lu));
    }

    @Override
    public Optional<Estudiante> findByDni(EntityManager em, Integer dni) {
        TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e FROM Estudiante e WHERE e.dni = :dni", Estudiante.class
        );
        query.setParameter("dni", dni);
        return query.getResultStream().findFirst();
    }

    @Override
    public List<Estudiante> findAllByGenero(EntityManager em, String genero) {
        TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e FROM Estudiante e WHERE e.genero = :genero", Estudiante.class
        );
        query.setParameter("genero", genero);
        return query.getResultList();
    }

    @Override
    public List<Estudiante> findAllOrderByApellido(EntityManager em) {
        TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e FROM Estudiante e ORDER BY e.apellido", Estudiante.class
        );
        return query.getResultList();
    }

    @Override
    public List<Estudiante> findAllByCarreraAndCiudad(EntityManager em, String nombreCarrera, String ciudad) {
        TypedQuery<Estudiante> query = em.createQuery("""
                SELECT e FROM Estudiante e
                JOIN e.inscripciones ec
                WHERE ec.carrera.nombre = :nombreCarrera
                AND LOWER(e.ciudadResidencia) = LOWER(:ciudad)
                """, Estudiante.class
        );
        query.setParameter("nombreCarrera", nombreCarrera);
        query.setParameter("ciudad", ciudad);
        return query.getResultList();
    }
}
