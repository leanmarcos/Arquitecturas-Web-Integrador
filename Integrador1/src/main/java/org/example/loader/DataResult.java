package org.example.loader;

import lombok.*;

/**
 * Define la estructura de los resultados generados durante el proceso de carga.
 *
 * Actúa como un objeto de salida similar a un DTO (Data Transfer Object), permitiendo agrupar y transportar la información resultante de la sincronización de datos de una manera estructurada.
 *
 * Su objetivo es separar los datos del resultado de la lógica utilizada para realizar la carga.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DataResult {
    private int clientes;
    private int productos;
    private int facturas;
    private int detalles;
}
