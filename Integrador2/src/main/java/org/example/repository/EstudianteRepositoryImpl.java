package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Carrera;
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

    public List<Estudiante> findStudentsByCarreraAndCity(EntityManager em, Carrera carrera, String city) {
        TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e " +
                        "FROM Estudiante e " +
                        "JOIN e.inscripciones ec " +
                        "WHERE ec.carrera = :carrera " +
                        "AND LOWER(e.ciudadResidencia) = LOWER(:city)",
                Estudiante.class
        );
        query.setParameter("carrera", carrera);
        query.setParameter("city", city);
        return query.getResultList();
    }
}
