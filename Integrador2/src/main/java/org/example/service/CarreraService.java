package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import org.example.dto.CarreraInscriptosResponseDTO;
import org.example.dto.CarreraRequestDTO;
import org.example.dto.CarreraResponseDTO;
import org.example.dto.EstudianteResponseDTO;
import org.example.exceptions.CarreraExistingException;
import org.example.exceptions.CarreraNotFoundException;
import org.example.exceptions.UnexpectedException;
import org.example.mapper.CarreraMapper;
import org.example.model.Carrera;
import org.example.reporte.CarreraReporteDTO;
import org.example.reporte.EventoCarrera;
import org.example.reporte.ReporteCarrerasMapper;
import org.example.reporte.TipoEvento;
import org.example.repository.CarreraRepository;
import org.example.utils.JPAUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public CarreraResponseDTO save(CarreraRequestDTO carreraDto) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();

            try {
                tx.begin();

                Optional<Carrera> existente =
                        repository.findByNombre(em, carreraDto.nombre());

                if (existente.isPresent()) {
                    throw new CarreraExistingException();
                }

                Carrera guardada = repository.save(em, CarreraMapper.toEntity(carreraDto))
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

    public List<CarreraInscriptosResponseDTO> obtenerCarrerasConCantInscriptos(){
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return repository.findCarreraWithEstudiantes(em);
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "El id de la carrera debe ser válido."
            );
        }
    }

    /**
     * Genera el reporte de inscriptos y egresados por año de cada carrera,
     * con las carreras en orden alfabético y los años en orden cronológico.
     */
    public List<CarreraReporteDTO> getCarreraReporteByAnio(){
        try (EntityManager em = JPAUtil.getEntityManager()){
            Map<CarreraReporteDTO.CarreraAnioKey, List<EventoCarrera>> eventosPorCarreraYAnio = repository.findFilasReporte(em).stream()
                    .flatMap(ReporteCarrerasMapper::toEventos)
                    .collect(Collectors.groupingBy(e -> new CarreraReporteDTO.CarreraAnioKey(e.carrera(), e.anio())));

            List<CarreraReporteDTO> reportes = new ArrayList<>();

            for(Map.Entry<CarreraReporteDTO.CarreraAnioKey, List<EventoCarrera>> entry : eventosPorCarreraYAnio.entrySet()){
                List<EstudianteResponseDTO> inscriptos = new ArrayList<>();
                List<EstudianteResponseDTO> egresados = new ArrayList<>();

                for(EventoCarrera evento : entry.getValue()){
                    if(TipoEvento.INSCRIPCION.equals(evento.tipo())){
                        inscriptos.add(evento.estudiante());
                    } else {
                        egresados.add(evento.estudiante());
                    }
                }

                CarreraReporteDTO reporte = CarreraReporteDTO.builder()
                        .carreraAnioKey(entry.getKey())
                        .inscriptos(inscriptos)
                        .egresados(egresados)
                        .build();
                reportes.add(reporte);
            }

            reportes.sort(Comparator.comparing((CarreraReporteDTO r) -> r.carreraAnioKey().carrera())
                    .thenComparing(r -> r.carreraAnioKey().anio()));
            return reportes;
        }
    }
}