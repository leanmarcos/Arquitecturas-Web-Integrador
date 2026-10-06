package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.EstudianteCarreraRequestDTO;
import org.example.dto.EstudianteCarreraResponseDTO;
import org.example.service.EstudianteCarreraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estudiantes/{dni}/inscripciones")
@RequiredArgsConstructor
public class EstudianteCarreraController {

    private final EstudianteCarreraService service;

    @PostMapping
    public ResponseEntity<EstudianteCarreraResponseDTO> matricular(@PathVariable Integer dni,
                                                                   @Valid @RequestBody EstudianteCarreraRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.matricular(dni, request));
    }
}
