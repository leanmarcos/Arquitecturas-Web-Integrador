package org.example.controller;

import org.example.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EstudianteController {

    @Autowired
    private EstudianteService service;
}
