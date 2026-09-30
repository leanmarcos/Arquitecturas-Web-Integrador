package org.example.loader;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.example.loader.DataResult.Conteo;
import org.example.loader.DataResult.Rechazo;
import org.example.mapper.CarreraMapper;
import org.example.mapper.EstudianteCarreraMapper;
import org.example.mapper.EstudianteMapper;
import org.example.service.CarreraService;
import org.example.service.EstudianteCarreraService;
import org.example.service.EstudianteService;
import org.example.utils.CsvImporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Carga los CSV de {@code /data} en la base pasando por los services, así cada fila se valida igual que un alta
 * por API.
 * <p>
 * Cada fila se guarda en su propia transacción (la abre el service): si una fila es inválida o la base la rechaza
 * (ej: inscripción duplicada), se registra el motivo en el {@link DataResult} y se sigue con la siguiente sin perder
 * lo ya cargado.
 */
@RequiredArgsConstructor
public class DataLoader {

    private static final String ESTUDIANTES_CSV = "/data/estudiantes.csv";
    private static final String CARRERAS_CSV = "/data/carreras.csv";
    private static final String INSCRIPCIONES_CSV = "/data/estudianteCarrera.csv";

    private final CarreraService carreraService;
    private final EstudianteService estudianteService;
    private final EstudianteCarreraService estudianteCarreraService;

    public DataResult cargar() {
        Conteo carreras = cargarFilas(CARRERAS_CSV,
                row -> carreraService.save(CarreraMapper.fromCsv(row)));

        Conteo estudiantes = cargarFilas(ESTUDIANTES_CSV,
                row -> estudianteService.create(EstudianteMapper.fromCsv(row)));

        Map<Long, String> nombresCarreraPorIdCsv = obtenerNombresCarreraPorIdCsv();
        Conteo inscripciones = cargarFilas(INSCRIPCIONES_CSV,
                row -> estudianteCarreraService.matricularEstudianteEnCarrera(
                        EstudianteCarreraMapper.fromCsv(row, nombresCarreraPorIdCsv)));

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
                rechazados.add(new Rechazo(row.getRecordNumber(), motivo(e)));
            }
        }
        return new Conteo(cargados, rechazados);
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

    /**
     * Mensaje de la causa raíz: los services envuelven los errores de la base (ej: violación de unique), y el
     * mensaje útil queda en la excepción más interna.
     */
    private String motivo(Throwable e) {
        Throwable causa = e;
        while (causa.getCause() != null) {
            causa = causa.getCause();
        }
        return causa.getMessage() != null ? causa.getMessage() : causa.getClass().getSimpleName();
    }
}
