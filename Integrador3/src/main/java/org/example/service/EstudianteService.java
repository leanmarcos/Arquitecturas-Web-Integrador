package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.EstudianteRequestDTO;
import org.example.dto.EstudianteResponseDTO;
import org.example.exceptions.DocumentException;
import org.example.exceptions.EstudianteNotFoundException;
import org.example.exceptions.GeneroNotFoundException;
import org.example.mapper.EstudianteMapper;
import org.example.model.Estudiante;
import org.example.model.EstudianteGenero;
import org.example.repository.EstudianteRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    Estudiante findEntityByDni(Integer dni){
        return repository.findByDni(dni)
                .orElseThrow(() -> new EstudianteNotFoundException(dni));
    }

    public List<EstudianteResponseDTO> searchEstudiantes(Sort sort) {
        return repository.findAll(sort).stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<EstudianteResponseDTO> searchByGenero(String genero){
        EstudianteGenero estudianteGenero = EstudianteGenero.from(genero).
                orElseThrow(() -> new GeneroNotFoundException(genero));

      return this.repository.findByGenero(estudianteGenero)
              .stream()
              .map(mapper::toDto)
              .toList();

    }

    public EstudianteResponseDTO findEstudianteByLu(Long lu) {
        return repository.findByLu(lu)
                .map(mapper::toDto)
                .orElseThrow(() -> new EstudianteNotFoundException(lu));
    }
}
