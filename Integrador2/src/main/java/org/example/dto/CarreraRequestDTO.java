package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public record CarreraRequestDTO (
    @NotBlank(message = "El nombre de la carrera es obligatorio")
    @Size(max = 255, message = "El nombre no debe superar los 255 caracteres")
    String nombre

){

}
