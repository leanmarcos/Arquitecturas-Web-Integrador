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

    public Estudiante findEntityByDni(Integer dni){
        return repository.findByDni(dni)
                .orElseThrow(() -> new EstudianteNotFoundException(dni));
    }

    public List<EstudianteResponseDTO> searchEstudiantes(Sort sort) {
        return repository.findAll(sort).stream()
                .map(mapper::toDto)
                .toList();
    }
}
