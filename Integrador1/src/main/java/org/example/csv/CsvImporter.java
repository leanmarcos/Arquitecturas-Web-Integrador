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

public class CsvImporter {

    public <T> List<T> importar(String resource, Function<CSVRecord, T> mapper) {
        try (InputStream is = getClass().getResourceAsStream(resource);
             InputStreamReader reader = new InputStreamReader(is);
             CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader)) {

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
