package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.EstudianteCarreraRequestDTO;
import org.example.dto.EstudianteCarreraResponseDTO;
import org.example.dto.EstudiantesPorCarreraResponseDTO;
import org.example.service.EstudianteCarreraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/inscripciones")
@RequiredArgsConstructor
public class EstudianteCarreraController {

    private final EstudianteCarreraService service;

    @PostMapping
    public ResponseEntity<EstudianteCarreraResponseDTO> matricular(@Valid @RequestBody EstudianteCarreraRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.matricular(request));
    }

    @GetMapping
    public ResponseEntity<EstudiantesPorCarreraResponseDTO> findByCarreraAndCiudad(@RequestParam String carrera,
                                                                                   @RequestParam String ciudad){
        return ResponseEntity.ok(service.findEstudiantesByCarreraAndCiudad(carrera, ciudad));
    }
}
