package org.example.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Lee un archivo CSV (con header) desde el classpath y convierte cada fila
 * en un objeto de dominio.
 *
 * <p>Separa el parseo del CSV (formato del archivo) de la construcción de
 * la entidad (a qué tipo se convierte cada fila): esta clase solo sabe leer
 * filas, quien la llama decide el mapeo pasando una lambda que implementa
 * {@link Function}.
 */
public class CsvImporter {

    /**
     * @param resource ruta del CSV dentro del classpath (ej: "/data/productos.csv")
     * @param mapper   convierte una fila del CSV ({@link CSVRecord}) en una instancia de T
     * @return una lista con una instancia de T por cada fila del CSV
     * @throws RuntimeException si el recurso no existe o falla la lectura del archivo
     *
     * <p>Ejemplo de uso con lambda:
     * <pre>{@code
     * List<Producto> productos = csvImporter.importar("/data/productos.csv",
     *     row -> new Producto(
     *         Integer.parseInt(row.get("idProducto")),
     *         row.get("nombre"),
     *         Float.parseFloat(row.get("valor"))));
     * }</pre>
     */
    public <T> List<T> importar(String resource, Function <CSVRecord, T> mapper) {
        InputStream is = getClass().getResourceAsStream(resource);
        if (is == null) {
            throw new RuntimeException("No se encontró el recurso: " + resource);
        }

        try (InputStreamReader reader = new InputStreamReader(is);
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .get()
                     .parse(reader)) {

            List<T> resultado = new ArrayList<>();
            for (CSVRecord row : parser) {
                resultado.add(mapper.apply(row));
            }
            return resultado;

        } catch (IOException e) {
            throw new RuntimeException("Error al leer el CSV: " + resource, e);
        }
    }
}
