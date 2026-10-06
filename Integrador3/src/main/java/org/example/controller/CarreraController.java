package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.CarreraInscriptosResponseDTO;
import org.example.dto.CarreraReporteDTO;
import org.example.dto.CarreraRequestDTO;
import org.example.dto.CarreraResponseDTO;
import org.example.service.CarreraService;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/carreras")
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraService service;

    @PostMapping
    public ResponseEntity<CarreraResponseDTO> create(@Valid @RequestBody CarreraRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/con-inscriptos")
    public ResponseEntity<List<CarreraInscriptosResponseDTO>> findAllOrderedByInscriptos(){
        return ResponseEntity.ok(service.findAllOrderedByInscriptos());
    }

    @GetMapping("/reporte")
    public ResponseEntity<List<CarreraReporteDTO>> generateReport(){
        return ResponseEntity.ok(service.generateReport());
    }
}
