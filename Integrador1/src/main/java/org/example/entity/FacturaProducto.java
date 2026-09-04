package org.example.entity;
import lombok.*;

/**
 * Representa la relación entre una factura y los productos que la componen, incluyendo la información necesaria para modelar dicha asociación.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FacturaProducto {
    private int idFactura;
    private int idProducto;
    private int cantidad;

}
