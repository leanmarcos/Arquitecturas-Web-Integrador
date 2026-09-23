package org.example.dto;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CarreraRequestDTO {

    @Getter
    @NotBlank(message = "El nombre de la carrera es obligatorio")
    @Size(max = 255, message = "El nombre no debe superar los 255 caracteres")
    private String nombre;

}
