package org.example.loader;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.example.exceptions.ValidationException;
import org.example.loader.DataResult.Conteo;
import org.example.loader.DataResult.Rechazo;
import org.example.mapper.CarreraMapper;
import org.example.mapper.EstudianteCarreraMapper;
import org.example.mapper.EstudianteMapper;
import org.example.service.CarreraService;
import org.example.service.EstudianteCarreraService;
import org.example.service.EstudianteService;
import org.example.utils.CsvImporter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Al levantar la aplicación, carga los CSV de {@code /data} en la base pasando por los services. Antes de cada alta
 * valida el DTO con las mismas anotaciones que usa la API ({@code @Valid} en el controller).
 * <p>
 * {@code CommandLineRunner} es el que hace que se ejecute la clase a darle run al programa
 * <p>
 * {@code @Profile("!test")} apaga la clase en los tests, así cada test arranca solo con los datos que carga él.
 * <p>
 * Si una fila es inválida o el service la rechaza (ej: inscripción duplicada), se registra el motivo en el
 * {@link DataResult} y se sigue con la siguiente sin perder lo ya cargado.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private static final String ESTUDIANTES_CSV = "/data/estudiantes.csv";
    private static final String CARRERAS_CSV = "/data/carreras.csv";
    private static final String INSCRIPCIONES_CSV = "/data/estudianteCarrera.csv";

    private final CarreraService carreraService;
    private final EstudianteService estudianteService;
    private final EstudianteCarreraService estudianteCarreraService;
    private final CarreraMapper carreraMapper;
    private final EstudianteMapper estudianteMapper;
    private final EstudianteCarreraMapper estudianteCarreraMapper;
    private final Validator validator;

    @Override
    public void run(String... args) {
        DataResult resultado = cargar();
        loguear("carreras", resultado.carreras());
        loguear("estudiantes", resultado.estudiantes());
        loguear("inscripciones", resultado.inscripciones());
    }

    public DataResult cargar() {
        Conteo carreras = cargarFilas(CARRERAS_CSV,
                row -> carreraService.create(validar(carreraMapper.fromCsv(row))));

        Conteo estudiantes = cargarFilas(ESTUDIANTES_CSV,
                row -> estudianteService.createEstudiante(validar(estudianteMapper.fromCsv(row))));

        Map<Long, String> nombresCarreraPorIdCsv = obtenerNombresCarreraPorIdCsv();
        Conteo inscripciones = cargarFilas(INSCRIPCIONES_CSV,
                row -> estudianteCarreraService.matricular(
                        Integer.parseInt(row.get("id_estudiante").trim()),
                        validar(estudianteCarreraMapper.fromCsv(row, nombresCarreraPorIdCsv))));

        return new DataResult(carreras, estudiantes, inscripciones);
    }

    /**
     * Aplica {@code guardarFila} a cada fila del CSV. Trae las filas sin mapear para que el DTO se arme dentro del
     * {@code try}: así una fila inválida queda registrada como rechazada y no corta la carga del resto.
     */
    private Conteo cargarFilas(String resource, Consumer<CSVRecord> guardarFila) {
        int cargados = 0;
        List<Rechazo> rechazados = new ArrayList<>();

        for (CSVRecord row : CsvImporter.importar(resource, Function.identity())) {
            try {
                guardarFila.accept(row);
                cargados++;
            } catch (RuntimeException e) {
                rechazados.add(new Rechazo(row.getRecordNumber(), e.getMessage()));
            }
        }
        return new Conteo(cargados, rechazados);
    }

    /**
     * Corre las anotaciones de validación del DTO, que fuera de un request HTTP no se ejecutan solas.
     *
     * @throws ValidationException con el mensaje de cada anotación que falló
     */
    private <T> T validar(T dto) {
        Set<ConstraintViolation<T>> errores = validator.validate(dto);
        if (!errores.isEmpty()) {
            List<String> mensajes = errores.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();
            throw new ValidationException(mensajes);
        }
        return dto;
    }

    /**
     * En {@code estudianteCarrera.csv} la carrera viene por su id en {@code carreras.csv}, que no coincide con el de
     * la base (autoincremental). Este mapa permite traducirlo al nombre, que es por lo que busca el service.
     */
    private Map<Long, String> obtenerNombresCarreraPorIdCsv() {
        return CsvImporter.importar(CARRERAS_CSV, Function.identity())
                .stream()
                .collect(Collectors.toMap(
                        row -> Long.parseLong(row.get("id_carrera").trim()),
                        row -> row.get("carrera").trim()
                ));
    }

    private void loguear(String nombre, Conteo conteo) {
        log.info("{}: {} cargados, {} rechazados", nombre, conteo.cargados(), conteo.rechazados().size());
        conteo.rechazados().forEach(rechazo -> log.warn("  fila {}: {}", rechazo.fila(), rechazo.motivo()));
    }
}
