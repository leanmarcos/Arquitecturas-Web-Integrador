package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.EstudianteCarreraService;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class EstudianteCarreraController {

    private final EstudianteCarreraService service;
}
