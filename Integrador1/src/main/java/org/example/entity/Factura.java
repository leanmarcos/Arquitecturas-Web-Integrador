package org.example.entity;
import lombok.*;

@Data // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los campos
@ToString // ToString

public class Factura {

    private int idFactura;
    private int idCliente;

}
