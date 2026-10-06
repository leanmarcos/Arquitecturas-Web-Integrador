package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.EstudianteRequestDTO;
import org.example.dto.EstudianteResponseDTO;
import org.example.exceptions.DocumentException;
import org.example.exceptions.EstudianteNotFoundException;
import org.example.mapper.EstudianteMapper;
import org.example.model.Estudiante;
import org.example.repository.EstudianteRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final EstudianteRepository repository;
    private final EstudianteMapper mapper;

    public EstudianteResponseDTO createEstudiante(EstudianteRequestDTO request){
        if (repository.existsByDni(request.dni())) {
            throw new DocumentException(request.dni());
        }
        Estudiante e = mapper.toEntity(request);
        return mapper.toDto(repository.save(e));
    }

    // Devuelve la entidad, no el DTO: es de paquete para que solo la usen otros services (ej: EstudianteCarreraService)
    Estudiante findEntityByDni(Integer dni){
        return repository.findByDni(dni)
                .orElseThrow(() -> new EstudianteNotFoundException(dni));
    }

    public List<EstudianteResponseDTO> searchEstudiantes(Sort sort) {
        return repository.findAll(sort).stream()
                .map(mapper::toDto)
                .toList();
    }

    public EstudianteResponseDTO findEstudianteByLu(Long lu) {
        return repository.findByLu(lu)
                .map(mapper::toDto)
                .orElseThrow(() -> new EstudianteNotFoundException(lu));
    }
}
