package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.CarreraService;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraService service;
}
