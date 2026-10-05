package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.EstudianteRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final EstudianteRepository repository;
}
