package org.example.dto;

import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ErrorResponseDTO {

    @NotNull
    @Setter
    private Integer errorCode;

    @NotBlank
    @Setter
    private String errorMessage;

    @Setter
    private String details;

}
