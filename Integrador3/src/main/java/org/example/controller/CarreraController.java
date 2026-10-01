package org.example.controller;

import org.example.service.CarreraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CarreraController {

    @Autowired
    private CarreraService service;
}
