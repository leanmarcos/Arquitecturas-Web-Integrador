package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import org.example.dto.CarreraInscriptosResponseDTO;
import org.example.dto.CarreraReporteDTO;
import org.example.dto.CarreraRequestDTO;
import org.example.dto.CarreraResponseDTO;
import org.example.dto.FilaReporteDTO;
import org.example.exceptions.CarreraExistingException;
import org.example.exceptions.CarreraNotFoundException;
import org.example.exceptions.UnexpectedException;
import org.example.mapper.CarreraMapper;
import org.example.model.Carrera;
import org.example.repository.CarreraRepository;
import org.example.utils.JPAUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

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

    /**
     * Genera un reporte cronológico y alfabético de inscriptos y egresados por carrera y por año.
     * <p>
     * Recupera las métricas de inscriptos y egresados desde el repositorio a través de dos
     * consultas agregadas independientes, consolidándolas en memoria mediante un {@link TreeMap}
     * para asegurar el ordenamiento alfabético por carrera (A-Z) y cronológico ascendente por año.
     *
     * @return Lista de {@link CarreraReporteDTO} con las estadísticas por carrera y año.
     */
    public List<CarreraReporteDTO> generarReporteCarreras() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<FilaReporteDTO> inscriptos = repository.findInscriptosPorAnio(em);
            List<FilaReporteDTO> egresados = repository.findEgresadosPorAnio(em);

            Map<CarreraReporteDTO.ClaveReporte, MetricasAnio> acumulador = new TreeMap<>();

            for (FilaReporteDTO fila : inscriptos) {
                CarreraReporteDTO.ClaveReporte clave = new CarreraReporteDTO.ClaveReporte(fila.nombreCarrera(), fila.anio());
                acumulador.computeIfAbsent(clave, k -> new MetricasAnio()).sumarInscriptos(fila.cantidad());
            }

            for (FilaReporteDTO fila : egresados) {
                CarreraReporteDTO.ClaveReporte clave = new CarreraReporteDTO.ClaveReporte(fila.nombreCarrera(), fila.anio());
                acumulador.computeIfAbsent(clave, k -> new MetricasAnio()).sumarEgresados(fila.cantidad());
            }

            List<CarreraReporteDTO> resultado = new ArrayList<>();
            for (Map.Entry<CarreraReporteDTO.ClaveReporte, MetricasAnio> entry : acumulador.entrySet()) {
                resultado.add(new CarreraReporteDTO(
                        entry.getKey().carrera(),
                        entry.getKey().anio(),
                        entry.getValue().getInscriptos(),
                        entry.getValue().getEgresados()
                ));
            }

            return resultado;
        }
    }

    /**
     * Estructura auxiliar para acumular inscriptos y egresados de forma fuertemente tipada.
     */
    private static class MetricasAnio {
        private long inscriptos = 0L;
        private long egresados = 0L;

        public void sumarInscriptos(long cant) {
            this.inscriptos += cant;
        }

        public void sumarEgresados(long cant) {
            this.egresados += cant;
        }

        public long getInscriptos() {
            return inscriptos;
        }

        public long getEgresados() {
            return egresados;
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