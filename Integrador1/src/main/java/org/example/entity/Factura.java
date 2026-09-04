package org.example.entity;
import lombok.*;

/**
 * Representa una factura y contiene los datos correspondientes a una operación de facturación.
 */
@Getter
@Setter
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los campos
@ToString // ToString
public class Factura {

    private int idFactura;
    private int idCliente;

}
