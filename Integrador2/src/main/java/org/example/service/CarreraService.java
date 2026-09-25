package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import org.example.dto.CarreraResponseDTO;
import org.example.exceptions.CarreraExistingException;
import org.example.exceptions.CarreraNotFoundException;
import org.example.exceptions.UnexpectedException;
import org.example.model.Carrera;
import org.example.repository.CarreraRepository;
import org.example.utils.JPAUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CarreraService {

    private CarreraRepository repository;

    public List<CarreraResponseDTO> getAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Carrera> carreras = repository.findAll(em);
            List<CarreraResponseDTO> dtos = new ArrayList<>();

            for (Carrera carrera : carreras) {
                dtos.add(new CarreraResponseDTO(carrera.getNombre()));
            }

            return dtos;
        }
    }

    public CarreraResponseDTO save(Carrera carrera) {
        validarCarrera(carrera);

        String nombre = carrera.getNombre().trim();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();

            try {
                tx.begin();

                Optional<Carrera> existente =
                        repository.findByNombre(em, nombre);

                if (existente.isPresent()) {
                    throw new CarreraExistingException();
                }

                Carrera guardada = repository.save(em, carrera)
                        .orElseThrow(UnexpectedException::new);

                tx.commit();

                return new CarreraResponseDTO(guardada.getNombre());

            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }

                throw e;
            }
        }
    }

    public CarreraResponseDTO getCarreraById(Long id) {
        validarId(id);

        try (EntityManager em = JPAUtil.getEntityManager()) {
            Carrera carrera = repository.findById(em, id)
                    .orElseThrow(() -> new CarreraNotFoundException(id));

            return new CarreraResponseDTO(carrera.getNombre());
        }
    }

    public CarreraResponseDTO delete(Carrera carrera) {
        if (carrera == null) {
            throw new IllegalArgumentException(
                    "La carrera es obligatoria."
            );
        }

        validarId(carrera.getId());

        try (EntityManager em = JPAUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();

            try {
                tx.begin();

                repository.findById(em, carrera.getId())
                        .orElseThrow(() ->
                                new CarreraNotFoundException(
                                        carrera.getId()
                                ));

                Carrera eliminada = repository
                        .deleteById(em, carrera.getId())
                        .orElseThrow(UnexpectedException::new);

                tx.commit();

                return new CarreraResponseDTO(eliminada.getNombre());

            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }

                throw e;
            }
        }
    }

    public Carrera findEntityByName(
            EntityManager em,
            String nombre
    ) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la carrera es obligatorio."
            );
        }

        return repository.findByNombre(em, nombre.trim())
                .orElseThrow(RuntimeException::new);
    }

    private void validarCarrera(Carrera carrera) {
        if (carrera == null) {
            throw new IllegalArgumentException(
                    "La carrera es obligatoria."
            );
        }

        String nombre = carrera.getNombre();

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la carrera es obligatorio."
            );
        }

        if (nombre.trim().length() > 255) {
            throw new IllegalArgumentException(
                    "El nombre de la carrera no puede superar los 255 caracteres."
            );
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "El id de la carrera debe ser válido."
            );
        }
    }
}