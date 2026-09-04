package org.example.entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los campos
@ToString // ToString

public class Factura {

    private int idFactura;
    private int idCliente;

}
