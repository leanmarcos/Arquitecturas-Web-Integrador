package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.dto.EstudianteCarreraResponseDTO;
import org.example.mapper.EstudianteCarreraMapper;
import org.example.model.Carrera;
import org.example.model.Estudiante;
import org.example.model.EstudianteCarrera;
import org.example.repository.EstudianteCarreraRepository;
import org.example.utils.JPAUtil;

import java.time.LocalDate;

public class EstudianteCarreraService {
    private final EstudianteCarreraRepository ecRepository;
    private final EstudianteService estudianteService;
    private final CarreraService carreraService;

    public EstudianteCarreraService(EstudianteCarreraRepository ecRepository,
                                    EstudianteService estudianteService,
                                    CarreraService carreraService){
        this.ecRepository = ecRepository;
        this.estudianteService = estudianteService;
        this.carreraService = carreraService;
    }

    public EstudianteCarreraResponseDTO matricularEstudianteEnCarrera(Integer dni, String nombreCarrera){
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            Estudiante estudiante = estudianteService.findEntityByDni(em, dni);
            Carrera carrera = carreraService.findEntityByName(em, nombreCarrera);
            EstudianteCarrera inscripcion = ecRepository.save(em, EstudianteCarrera.builder()
                    .estudiante(estudiante)
                    .carrera(carrera)
                    .fechaInscripcion(LocalDate.now())
                    .build());
            tx.commit();
            return EstudianteCarreraMapper.toDto(inscripcion);
        }catch(RuntimeException e){
            if(tx.isActive()){
                tx.rollback();
            }
            throw e;
        }finally {
            em.close();
        }
    }
}
