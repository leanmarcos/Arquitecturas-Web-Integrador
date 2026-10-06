package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.exceptions.CarreraExistingException;
import org.example.exceptions.CarreraNotFoundException;
import org.example.mapper.CarreraMapper;
import org.example.model.Carrera;
import org.example.repository.CarreraRepository;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<CarreraReporteDTO> generateReport(){
        Map<ClaveReporte, CarreraReporteDTO> reporte = new HashMap<>();

        List<FilaReporteDTO> filaInscriptos = repository.findCarrerasGroupByCantInscriptos();
        List<FilaReporteDTO> filaEgresados = repository.findCarrerasGroupByCantEgresados();

        filaInscriptos.forEach(fila -> {
                reporte.put(fila.clave(), new CarreraReporteDTO(fila.nombreCarrera(), fila.anio(), fila.cantidad(), 0L ));
            }
        );

        filaEgresados.forEach(fila -> {
                reporte.merge(
                        fila.clave(),
                        //Si no existia la clave (para esa carrera ese anio no habia inscriptos):
                        new CarreraReporteDTO(fila.nombreCarrera(), fila.anio(), 0L, fila.cantidad()),

                        //Si la clave ya existia (para esa carrera ese anio ya habia inscriptos), se combinan ambos
                        (viejo, nuevo) -> new CarreraReporteDTO(
                                viejo.nombreCarrera(),
                                viejo.anio(),
                                viejo.inscriptos(), //mantiene los inscriptos que estaban
                                nuevo.egresados() //devuelve fila.cantidad(), agrega los egresados que no estaban
                        )
                );
            }
        );

        //De esta manera o declarando un TreeMap al principio tambien sirve
        return reporte.values().stream()
                .sorted(Comparator
                        .comparing(CarreraReporteDTO::nombreCarrera)
                        .thenComparing(CarreraReporteDTO::anio)
                )
                .toList();
    }
}
