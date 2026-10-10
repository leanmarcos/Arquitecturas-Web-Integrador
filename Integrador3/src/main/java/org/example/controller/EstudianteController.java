package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.EstudianteRequestDTO;
import org.example.dto.EstudianteResponseDTO;
import org.example.service.EstudianteService;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;


    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> create(@Valid @RequestBody EstudianteRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(estudianteService.createEstudiante(request));
    }

    @GetMapping
    public ResponseEntity<List<EstudianteResponseDTO>> search(
            @SortDefault(sort = "apellido", direction = Sort.Direction.ASC) Sort sort
    ){
        return ResponseEntity.ok(estudianteService.searchEstudiantes(sort));
    }

    @GetMapping("/{lu}")
    public ResponseEntity<EstudianteResponseDTO> findByLu(
            @PathVariable Long lu
    ){
        return ResponseEntity.ok(estudianteService.findEstudianteByLu(lu));
    }

    @GetMapping("/estudiante/{genero}")
    public ResponseEntity<List<EstudianteResponseDTO>> searchByGenero(@PathVariable String genero){
        return ResponseEntity.ok(this.estudianteService.searchByGenero(genero));
    }
}
