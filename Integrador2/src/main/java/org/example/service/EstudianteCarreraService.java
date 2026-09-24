package org.example.service;

import jakarta.persistence.EntityManager;
import org.example.dto.EstudianteResponseDTO;
import org.example.model.Carrera;

import java.util.ArrayList;
import java.util.List;
import org.example.dto.EstudianteResponseDTO;
import org.example.mapper.EstudianteMapper;
import org.example.model.Estudiante;
import org.example.repository.EstudianteRepositoryImpl;
import org.example.utils.JPAUtil;

public class EstudianteCarreraService {

    private EstudianteRepositoryImpl studentRepository;

    //Recuperar estudiantes de una carrera filtrados por ciudad
    public List<EstudianteResponseDTO> getCareerStudentsByCity(Carrera carrera, String city){
        if (carrera == null || carrera.getId() == null) throw new IllegalArgumentException("La carrera es obligatoria.");
        if (city == null || city.isBlank()) throw new IllegalArgumentException("La ciudad es obligatoria.");

        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Estudiante> students = studentRepository.findStudentsByCarreraAndCity(em, carrera, city.trim());

            List<EstudianteResponseDTO> responseDTOs = new ArrayList<>();
            for (Estudiante student : students) {
                responseDTOs.add(EstudianteMapper.toDto(student));
            }
            return responseDTOs;
        }
    }
}
