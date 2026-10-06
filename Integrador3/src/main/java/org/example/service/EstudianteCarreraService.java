package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.EstudianteCarreraRequestDTO;
import org.example.dto.EstudianteCarreraResponseDTO;
import org.example.dto.EstudianteResponseDTO;
import org.example.dto.EstudiantesPorCarreraResponseDTO;
import org.example.exceptions.EstudianteCarreraExistingException;
import org.example.mapper.EstudianteCarreraMapper;
import org.example.mapper.EstudianteMapper;
import org.example.model.Carrera;
import org.example.model.Estudiante;
import org.example.model.EstudianteCarrera;
import org.example.model.EstudianteCarreraId;
import org.example.repository.EstudianteCarreraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstudianteCarreraService {

    private final EstudianteCarreraRepository repository;
    private final EstudianteService estudianteService;
    private final CarreraService carreraService;
    private final EstudianteCarreraMapper estudianteCarreraMapper;
    private final EstudianteMapper estudianteMapper;

    @Transactional
    public EstudianteCarreraResponseDTO matricular(EstudianteCarreraRequestDTO request){
        Estudiante estudiante = estudianteService.findEntityByDni(request.dni());
        Carrera carrera = carreraService.findEntityByNombre(request.nombreCarrera());

        EstudianteCarreraId id = new EstudianteCarreraId(estudiante.getLu(), carrera.getId()); // clave compuesta (@EmbeddedId): usa la LU, la sacamos del estudiante
        if (repository.existsById(id)) { // sin esto, save() con la clave repetida pisa la inscripción en vez de fallar
            throw new EstudianteCarreraExistingException(estudiante.getDni(), carrera.getNombre());
        }

        EstudianteCarrera inscripcion = estudianteCarreraMapper.toEntity(request, estudiante, carrera);
        return estudianteCarreraMapper.toDto(repository.save(inscripcion));
    }

    public EstudiantesPorCarreraResponseDTO findEstudiantesByCarreraAndCiudad(String nombreCarrera, String ciudad) {
        Carrera carrera = carreraService.findEntityByNombre(nombreCarrera);
        List<EstudianteResponseDTO> estudiantes =
                repository.findEstudiantesByCarreraAndCiudad(carrera.getId(), ciudad.trim()).stream()
                        .map(estudianteMapper::toDto)
                        .toList();
        return estudianteCarreraMapper.toDto(estudiantes, carrera.getNombre(), ciudad.trim());
    }
}
