package org.example.entity;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString

public class FacturaProducto {

    private int idFactura;
    private int idProducto;
    private int cantidad;

}
