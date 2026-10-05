package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.EstudianteService;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService service;
}
