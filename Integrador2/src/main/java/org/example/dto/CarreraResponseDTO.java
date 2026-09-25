package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
public record CarreraResponseDTO(
    @NotBlank(message = "El nombre de la carrera es obligatorio")
    @Size(max = 255, message = "El nombre no debe superar los 255 caracteres")
    String nombre
){

}
