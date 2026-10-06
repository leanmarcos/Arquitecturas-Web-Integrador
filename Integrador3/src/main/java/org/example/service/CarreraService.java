package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CarreraInscriptosResponseDTO;
import org.example.dto.CarreraRequestDTO;
import org.example.dto.CarreraResponseDTO;
import org.example.exceptions.CarreraExistingException;
import org.example.exceptions.CarreraNotFoundException;
import org.example.mapper.CarreraMapper;
import org.example.model.Carrera;
import org.example.repository.CarreraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarreraService {

    private final CarreraRepository repository;
    private final CarreraMapper mapper;

    public CarreraResponseDTO create(CarreraRequestDTO request){
        Carrera carrera = mapper.toEntity(request);
        if (repository.existsByNombre(carrera.getNombre())) {
            throw new CarreraExistingException(carrera.getNombre());
        }
        return mapper.toDto(repository.save(carrera));
    }

    // Devuelve la entidad, no el DTO: es de paquete para que solo la usen otros services (ej: EstudianteCarreraService)
    Carrera findEntityByNombre(String nombre){
        return repository.findByNombre(nombre.trim())
                .orElseThrow(() -> new CarreraNotFoundException(nombre));
    }

    public List<CarreraInscriptosResponseDTO> findAllOrderedByInscriptos() {
        return repository.findAllOrderedByInscriptos();
    }
}
