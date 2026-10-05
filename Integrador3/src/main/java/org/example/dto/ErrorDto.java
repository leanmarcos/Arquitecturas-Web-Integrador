package org.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) //para cuando detail es null
public record ErrorDto(
        String error,
        String message,
        Integer status,
        Map<String, Object> detail
) {}

