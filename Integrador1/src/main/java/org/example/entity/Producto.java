package org.example.entity;
import lombok.*;

/**
 * Representa un producto y contiene la información asociada a cada registro de producto.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Producto {

    private int idProducto;
    private String nombre;
    private Float valor;

}

