package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.EstudianteCarreraRequestDTO;
import org.example.dto.EstudianteCarreraResponseDTO;
import org.example.exceptions.EstudianteCarreraExistingException;
import org.example.mapper.EstudianteCarreraMapper;
import org.example.model.Carrera;
import org.example.model.Estudiante;
import org.example.model.EstudianteCarrera;
import org.example.model.EstudianteCarreraId;
import org.example.repository.EstudianteCarreraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EstudianteCarreraService {

    private final EstudianteCarreraRepository repository;
    private final EstudianteService estudianteService;
    private final CarreraService carreraService;
    private final EstudianteCarreraMapper mapper;

    @Transactional
    public EstudianteCarreraResponseDTO matricular(Integer dni, EstudianteCarreraRequestDTO request){
        Estudiante estudiante = estudianteService.findEntityByDni(dni);
        Carrera carrera = carreraService.findEntityByNombre(request.nombreCarrera());

        EstudianteCarreraId id = new EstudianteCarreraId(estudiante.getLu(), carrera.getId()); // clave compuesta (@EmbeddedId): usa la LU, la sacamos del estudiante
        if (repository.existsById(id)) { // sin esto, save() con la clave repetida pisa la inscripción en vez de fallar
            throw new EstudianteCarreraExistingException(estudiante.getDni(), carrera.getNombre());
        }

        EstudianteCarrera inscripcion = mapper.toEntity(request, estudiante, carrera);
        return mapper.toDto(repository.save(inscripcion));
    }
}
