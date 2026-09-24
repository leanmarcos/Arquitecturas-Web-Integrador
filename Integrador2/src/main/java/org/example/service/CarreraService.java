package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.dto.CarreraResponseDTO;
import org.example.model.Carrera;
import org.example.repository.CarreraRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CarreraService {
    private CarreraRepository repository;

    public List<Carrera> getAll(){
        List<Carrera> carreras = new ArrayList<Carrera>();
        carreras =  repository.findAll();
        return carreras;
    }

    public CarreraResponseDTO save(Carrera carrera){
            // Verificar reglas del negocio de carrera

            Optional<Carrera> car = this.repository.save(carrera);

            if(car.isPresent()){
                CarreraResponseDTO dto = new CarreraResponseDTO();
                return dto;
            }

        return null;
    }

    public CarreraResponseDTO getCarreraById(Long id){
            Optional<Carrera> car = this.repository.findById(id);

            if(car.isPresent()){
                CarreraResponseDTO dto = new CarreraResponseDTO();
                return dto;
            }

            return null;
    }

    public CarreraResponseDTO delete(Carrera carrera){
        Optional<Carrera> car = this.repository.findById(carrera.getId());

        if(car.isPresent()){
            Optional<Carrera> deleted = this.repository.deleteById(carrera.getId());

            if(deleted.isPresent()){
                CarreraResponseDTO dto = new CarreraResponseDTO();

                // Buscar si sigue existiendo
                return dto;
            }
        }

        return null;
    }


    Carrera findEntityByName(EntityManager em, String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la carrera es obligatorio.");
        }
        return this.repository.findByNombre(em, nombre)
                .orElseThrow(() -> new EntityNotFoundException("Carrera no encontrada con nombre: " + nombre));
    }
}
