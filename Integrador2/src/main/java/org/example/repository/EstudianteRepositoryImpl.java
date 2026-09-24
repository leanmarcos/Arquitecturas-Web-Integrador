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
    public List<Estudiante> findAllByGenero(EntityManager em, String genero) {
        TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e FROM Estudiante e WHERE e.genero = :genero", Estudiante.class
        );
        query.setParameter("genero", genero);
        return query.getResultList();
    }
}
