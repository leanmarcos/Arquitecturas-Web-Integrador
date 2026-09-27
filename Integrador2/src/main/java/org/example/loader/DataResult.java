package org.example.loader;

import java.util.List;

/**
 * Resultado de una ejecución de {@link DataLoader}: por cada CSV, cuántas filas se cargaron y cuáles se rechazaron
 * y por qué.
 */
public record DataResult(
        Conteo carreras,
        Conteo estudiantes,
        Conteo inscripciones
) {
    /**
     * @param rechazados en el mismo orden que el CSV
     */
    public record Conteo(int cargados, List<Rechazo> rechazados) {
    }

    /**
     * @param fila   número de registro en el CSV, sin contar el header ({@code CSVRecord.getRecordNumber()})
     * @param motivo mensaje de la causa raíz del error
     */
    public record Rechazo(long fila, String motivo) {
    }
}
