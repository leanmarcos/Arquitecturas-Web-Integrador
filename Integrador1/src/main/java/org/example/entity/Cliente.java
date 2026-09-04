package org.example.entity;
import lombok.*;

/**
 * Representa un cliente del sistema y contiene los atributos necesarios para modelar la información de un registro de cliente.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Cliente {

    private int idCliente;
    private String nombre;
    private String email;

}
