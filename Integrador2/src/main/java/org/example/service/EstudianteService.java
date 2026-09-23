package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import org.example.dto.EstudianteRequestDTO;
import org.example.dto.EstudianteResponseDTO;
import org.example.mapper.EstudianteMapper;
import org.example.model.Estudiante;
import org.example.repository.EstudianteRepository;
import org.example.utils.JPAUtil;

public class EstudianteService {
    private final EstudianteRepository estudianteRepository;
    public EstudianteService (EstudianteRepository estudianteRepository){
        this.estudianteRepository = estudianteRepository;
    }

    public EstudianteResponseDTO findByLu(Long lu) {
        if (lu == null) {
            throw new IllegalArgumentException("La LU es obligatoria.");
        }
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return estudianteRepository.findByLu(em, lu)
                    .map(EstudianteMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado con LU: " + lu));
        }
    }

    public EstudianteResponseDTO create(EstudianteRequestDTO estudianteDto){
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            Estudiante eNuevo = EstudianteMapper.toEntity(estudianteDto);
            Estudiante eGuardado = estudianteRepository.save(em, eNuevo);
            tx.commit();
            return EstudianteMapper.toDto(eGuardado);
        }catch(Exception e){
            if(tx.isActive()){
                tx.rollback();
            }
            throw new RuntimeException("Error al crear el estudiante", e);
        }finally {
            em.close();
        }

    }
}
