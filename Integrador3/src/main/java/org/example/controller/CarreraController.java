package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Carrera")
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraService service;

    @Operation(summary = "Crear carrera" , description = "Crea carrera")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Carrera creada correctamente"),
            @ApiResponse(responseCode = "400" , description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Carrera ya existente")
    })
    @PostMapping
    public ResponseEntity<CarreraResponseDTO> create(@Valid @RequestBody CarreraRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Obtener carreras", description = "Obtener carreras ordenadas por cantidad de inscriptos y nombre")
    @ApiResponse(responseCode = "200", description = "Lista de carreras")
    @GetMapping("/con-inscriptos")
    public ResponseEntity<List<CarreraInscriptosResponseDTO>> findAllOrderedByInscriptos(){
        return ResponseEntity.ok(service.findAllOrderedByInscriptos());
    }

    @Operation(summary = "Generar reporte", description = "Reporte de carreras con información de inscriptos y egresados por año, ordenado alfabetica y cronologicamente")
    @ApiResponse(responseCode = "200", description = "Reporte generado")
    @GetMapping("/reporte")
    public ResponseEntity<List<CarreraReporteDTO>> generateReport(){
        return ResponseEntity.ok(service.generateReport());
    }
}
