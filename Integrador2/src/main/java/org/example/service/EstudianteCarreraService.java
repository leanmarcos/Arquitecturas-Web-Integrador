package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.dto.EstudianteCarreraRequestDTO;
import org.example.dto.EstudianteCarreraResponseDTO;
import org.example.exceptions.EstudianteCarreraExistingException;
import org.example.mapper.EstudianteCarreraMapper;
import org.example.model.Carrera;
import org.example.model.Estudiante;
import org.example.model.EstudianteCarrera;
import org.example.repository.EstudianteCarreraRepository;
import org.example.utils.JPAUtil;
import org.example.utils.PersistenceUtils;

public class EstudianteCarreraService {
    private final EstudianteCarreraRepository ecRepository;
    private final EstudianteService estudianteService;
    private final CarreraService carreraService;

    public EstudianteCarreraService(EstudianteCarreraRepository ecRepository, EstudianteService estudianteService, CarreraService carreraService){
        this.ecRepository = ecRepository;
        this.estudianteService = estudianteService;
        this.carreraService = carreraService;
    }

    /**
     * Matricula un estudiante en una carrera con los años del DTO.
     * <p>
     * No consulta si la inscripción ya existe: de eso se encarga la restricción unique de
     * {@link EstudianteCarrera}, que además evita duplicados entre dos altas simultáneas. Si la base rechaza la fila
     * se hace rollback; una violación del unique se traduce a {@link EstudianteCarreraExistingException} y cualquier
     * otro error se relanza como está.
     */
    public EstudianteCarreraResponseDTO matricularEstudianteEnCarrera(EstudianteCarreraRequestDTO inscripcionDto){
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Estudiante estudiante = estudianteService.findEntityByDni(em, inscripcionDto.dni());
            Carrera carrera = carreraService.findEntityByName(em, inscripcionDto.nombreCarrera());

            EstudianteCarrera inscripcion = ecRepository.save(em, EstudianteCarrera.builder()
                    .estudiante(estudiante)
                    .carrera(carrera)
                    .anioInscripcion(inscripcionDto.anioInscripcion())
                    .anioGraduacion(inscripcionDto.anioGraduacion())
                    .build());

            tx.commit();

            return EstudianteCarreraMapper.toDto(inscripcion);

        } catch(RuntimeException e){
            if (tx.isActive()){
                tx.rollback();
            }
            // la única restricción unique de la tabla es la del par estudiante-carrera
            if (PersistenceUtils.esViolacionDeUnique(e)) {
                throw new EstudianteCarreraExistingException();
            }
            throw e;
        }finally {
            em.close();
        }
    }
}
