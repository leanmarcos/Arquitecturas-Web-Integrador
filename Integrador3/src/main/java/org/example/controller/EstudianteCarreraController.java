package org.example.controller;

import org.example.service.EstudianteCarreraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EstudianteCarreraController {

    @Autowired
    private EstudianteCarreraService service;
}
